import lzo
import struct

from .encoding import crc8

PAPER_WIDTH = 384
BLOCK_LINES = 20
GAP_LINES = 64
DARK_PIXEL_THRESHOLD = 128  # Pixels with value below this are considered dark (printed)

def create_command(command_id: int, payload: bytes) -> bytes:
    command = bytearray()
    command.extend([0x51, 0x78, command_id, 0x00])  # Header
    command.extend(struct.pack('<H', len(payload)))  # Payload length as two bytes in little-endian
    command.extend(payload)
    command.append(crc8(command, 6, len(payload)))  # Payload checksum
    command.append(0xFF)  # Final byte

    return bytes(command)

def create_quality_command(quality: int = 4) -> bytes:
    # Quality/heat energy: levels 1 (lightest) to 5 (darkest), encoded as 0x31–0x35
    quality = max(1, min(5, quality))
    return create_command(0xA4, bytes([0x30 + quality]))

def create_speed_command(speed: int = 25) -> bytes:
    # Print speed: one byte value
    return create_command(0xBD, bytes([speed]))

def create_dev_state_command() -> bytes:
    # Query printer status (battery, paper, overheat)
    return create_command(0xA3, bytes([0x00]))

def create_feed_paper_command(lines: int) -> bytes:
    # Two bytes value in little-endian
    payload = struct.pack("<H", lines)

    return create_command(0xA1, payload)

def create_print_lines_command(data: bytes) -> bytes:
    # Compress data using LZO
    compressed = lzo.compress(data, 1, False)
    # Pack length of original and compressed data, and compressed data into payload
    payload = struct.pack('<HH', len(data), len(compressed)) + compressed

    return create_command(0xCF, payload)

def create_print_lines_commands(data: bytes, line_len: int, block_lines: int) -> bytes:
    # Add a white padding line to prevent dark-line artifacts at the start of the print
    prepared = bytes([0xFF] * line_len) + data
    
    # Calculer la longueur sur les données préparées pour ne pas oublier la dernière ligne
    total_len = len(prepared)
    block_len: int = line_len * block_lines

    if total_len % line_len != 0:
        raise ValueError(f"Printing data length must be a multiple of line len ({line_len})")

    commands = bytearray()

    for block_start in range(0, total_len, block_len):
        block_end = block_start + block_len
        block = prepared[block_start:block_end]
        packed = bytearray()

        # Pack two pixels into one byte
        for i in range(0, len(block), 2):
            p0 = block[i]
            # Utiliser 255 (blanc) par défaut au lieu de 0 (noir)
            p1 = block[i + 1] if i + 1 < len(block) else 255
            
            # 1. Inversion de chaleur : Blanc (255) -> 0, Noir (0) -> 15
            heat0 = 15 - (p0 >> 4)
            heat1 = 15 - (p1 >> 4)
            
            # 2. Nibble Swap : p1 prend les 4 bits de gauche, p0 les 4 bits de droite
            packed.append((heat1 << 4) | heat0)

        commands += create_print_lines_command(bytes(packed))

    return bytes(commands)

def create_print_commands(data: bytes, quality: int = 4, speed: int = 25) -> bytes:
    """Assemble the full command sequence to send to the printer.

    Args:
        data: Raw pixel data, one byte per pixel (0 = black, 255 = white).
              Length must be a multiple of PAPER_WIDTH (384).
        quality: Print darkness/heat energy, from 1 (lightest) to 5 (darkest).
        speed: Print speed byte value; lower values produce slower, darker output.
    """
    commands = bytearray()
    # Set print quality (heat energy) and speed
    commands += create_quality_command(quality)
    commands += create_speed_command(speed)
    # Add commands to print the image data in blocks
    commands += create_print_lines_commands(data, PAPER_WIDTH, BLOCK_LINES)
    # Add a command to feed the paper after printing
    commands += create_feed_paper_command(GAP_LINES)
    # Query printer status at the end
    commands += create_dev_state_command()

    return bytes(commands)
