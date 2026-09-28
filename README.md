# Java File Packer-Unpacker

A simple Java-based file packing and unpacking project that combines multiple supported files into a single packed file and restores them back when required.

## Features

* Pack multiple files into a single packed file
* Unpack files from the packed file
* Supports `.txt`, `.c`, `.cpp`, `.java`, `.py`, and `.pdf` files
* Uses file headers to store file name and file size
* Uses XOR transformation for stored file data
* Uses 1024-byte buffers for file processing
* Includes basic input and file validation
* Uses automatic resource management with try-with-resources
* Command-line interface (CUI)

## Technologies Used

* Java
* File Handling
* Java Collections
* Exception Handling
* Byte Stream
* XOR Operation

## Project Structure

```text
Java-File-Packer-Unpacker/
│
├── README.md
├── .gitignore
│
└── src/
    ├── FilePacker.java
    └── FileUnpacker.java
```

## How to Run

Compile the files:

```bash
javac FilePacker.java
javac FileUnpacker.java
```

Run the packer:

```bash
java FilePacker
```

Run the unpacker:

```bash
java FileUnpacker
```

## Packing

The packer takes:

1. Source folder name
2. Packed file name

Example:

```text
Enter folder name: TestFiles
Enter packed file name: PackedFile.pak

Packing completed successfully!
```

## Unpacking

The unpacker takes the packed file name.

Example:

```text
Enter packed file name: PackedFile.pak

Unpacking completed successfully!
```

## Note

This project is created for learning and demonstrating Java file handling, byte streams, file packing/unpacking, and basic data transformation.
