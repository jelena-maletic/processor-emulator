; Test program

start:
    IN R1                   ; Reads a character from the keyboard into R1
    MOV [0x100], R1

    MOV R2, 0x20
    MOV [0x108], R2

    MOV R3, [0x100]
    MOV R4, [0x108]
    ADD R3, R4

    MOV [0x110], R3

    MOV R4, [0x110]
    SUB R4, 10
    MOV [0x118], R4

    MOV R2, 5
    MUL R4, R2
    MOV [0x120], R4

    MOV R2, 2
    DIV R4, R2
    MOV [0x128], R4

    CMP R4, 100
    JL less

    MOV [0x130], R4
    JMP next

less:
    MOV R4, 42
    MOV [0x138], R4

next:
    OUT R3
    OUT R4

    MOV R1, [0x100]
    MOV R2, [0x108]
    MOV R3, [0x110]
    MOV R4, [0x118]
    MOV [0x140], R1
    MOV [0x148], R2
    MOV [0x150], R3
    MOV [0x158], R4

    NOT R1
    AND R1, 100

    HALT