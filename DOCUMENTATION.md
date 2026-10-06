# Java File Packer-Unpacker Documentation

## 1. Project Overview

Java File Packer-Unpacker is a **CUI-based Java project** that packs multiple supported files into a single packed file and extracts them back when required.

The project demonstrates:

* Java file handling
* Byte streams
* Collections
* Exception handling
* XOR-based data transformation
* Buffer-based file processing
* Resource management
* Input validation

---

## 2. Project Structure

```text
Java-File-Packer-Unpacker/
│
├── README.md
├── DOCUMENTATION.md
├── .gitignore
│
└── src/
    ├── FilePacker.java
    └── FileUnpacker.java
```

---

## 3. Supported File Types

The project currently supports:

```text
.txt
.c
.cpp
.java
.py
.pdf
```

Unsupported file types are skipped during packing.

Directories are also skipped during the packing process.

The generated packed file itself is excluded from the packing operation.

---

## 4. File Packing

`FilePacker.java` reads supported files from the specified folder and stores them in a single `.pak` file.

### Packing Process

```text
Source Folder
      ↓
Validate Folder & Packed File
      ↓
Read Files
      ↓
Skip Directories
      ↓
Skip Unsupported Files
      ↓
Create 100-Byte Header
      ↓
Read File in 1024-Byte Chunks
      ↓
Apply XOR Transformation
      ↓
Write Header + Transformed Data
      ↓
Packed .pak File
```

### Detailed Steps

1. Validate the source folder.
2. Validate the packed-file name.
3. Verify that the packed file uses the `.pak` extension.
4. Read files present in the source folder.
5. Skip directories.
6. Skip unsupported file extensions.
7. Skip the output `.pak` file itself.
8. Create a fixed-size 100-byte header for each supported file.
9. Store the file name and original file size in the header.
10. Read the source file using a 1024-byte buffer.
11. Apply XOR transformation to each chunk.
12. Write the transformed data to the packed file.
13. Maintain statistics such as packed files, skipped files, and total size.

The current implementation performs chunk-based processing using a 1024-byte buffer. 

---

## 5. Packed File Header

Each file stored inside the packed file contains a header.

### Header Format

```text
FileName#FileSize
```

Example:

```text
sample.txt#150
```

The header is padded with spaces so that each file header occupies exactly **100 bytes**.

The header stores:

* Original file name
* Original file size

The packer validates that the header fits within the fixed 100-byte area before writing it. 

### Packed File Structure

Conceptually:

```text
┌──────────────────────────────┐
│       100 Byte Header        │
├──────────────────────────────┤
│   Transformed File Data      │
├──────────────────────────────┤
│       100 Byte Header        │
├──────────────────────────────┤
│   Transformed File Data      │
├──────────────────────────────┤
│             ...              │
└──────────────────────────────┘
```

---

## 6. File Unpacking

`FileUnpacker.java` reads the packed `.pak` file and recreates the original files.

### Unpacking Process

```text
Packed .pak File
      ↓
Validate Packed File
      ↓
Read 100-Byte Header
      ↓
Extract File Name & File Size
      ↓
Create Output File
      ↓
Read Data in Chunks
      ↓
Reverse XOR Transformation
      ↓
Write Restored Data
      ↓
Repeat for Next File
```

### Detailed Steps

1. Validate the packed file.
2. Read exactly 100 bytes for the header.
3. Extract the file name.
4. Extract the original file size.
5. Validate the header information.
6. Handle existing output files.
7. Read the stored file data in chunks.
8. Apply the XOR transformation using the same key.
9. Write the restored data to the output file.
10. Continue until all files stored in the packed file are processed.

### Existing File Handling

When an extracted file already exists, the unpacker provides options to:

```text
1. Overwrite
2. Skip
3. Cancel
```

This prevents accidental overwriting without user confirmation.

---

## 7. XOR Transformation

The project uses XOR as a basic data transformation technique.

```java
byte XOR_KEY = 65;
```

### During Packing

```text
Original Data
      ↓
XOR with Key 65
      ↓
Transformed Data
      ↓
Packed File
```

### During Unpacking

```text
Transformed Data
      ↓
XOR with Key 65
      ↓
Original Data
      ↓
Extracted File
```

The same XOR operation reverses the transformation because:

```text
(A XOR K) XOR K = A
```

> **Note:** XOR transformation in this project is for learning purposes and should not be considered secure encryption.

---

## 8. Buffer-Based Processing

The project processes file data using a **1024-byte buffer**.

```java
byte Buffer[] = new byte[1024];
```

Instead of loading an entire file into memory, the data is processed in smaller chunks.

### Packing

```text
File
 ↓
1024 bytes
 ↓
XOR
 ↓
Write
 ↓
Next 1024 bytes
 ↓
...
```

### Unpacking

```text
Packed Data
 ↓
1024 bytes
 ↓
Reverse XOR
 ↓
Write
 ↓
Next chunk
 ↓
...
```

This approach reduces memory usage and allows the application to handle larger files more efficiently.

---

## 9. Resource Management

The project uses **try-with-resources** for file streams.

Example:

```java
try(FileInputStream fiobj = new FileInputStream(file))
{
    // File processing
}
```

The output stream is also handled using try-with-resources:

```java
try(FileOutputStream foobj = new FileOutputStream(packedFile))
{
    // Packing process
}
```

Streams are automatically closed after processing, helping prevent resource leaks.

---

## 10. Input Validation

The project performs validation for several situations.

### Packer Validation

* Empty folder name
* Empty packed-file name
* Non-existing source folder
* Path that is not a directory
* Packed-file path pointing to a directory
* Invalid packed-file name
* Missing `.pak` extension
* File names exceeding the 100-byte header limit
* Unsupported file extensions
* Empty folder containing no supported files

The `.pak` validation and source-folder validation are implemented in the current `FilePacker`. 

### Unpacker Validation

The unpacker also validates:

* Packed file existence
* Packed file type
* Header size
* Header contents
* File name
* File size
* Unexpected end of packed data
* Invalid user choices during overwrite handling

---

## 11. File Statistics

The packer maintains statistics during the packing operation:

```text
PackedFileCount
SkippedFileCount
TotalSize
```

These values can be retrieved using getter methods.

Example output:

```text
========================================
           PACKING COMPLETED
========================================

  Packed Files   : 3
  Skipped Files  : 2
  Total Size     : 15.42 KB

----------------------------------------
  Packed File    : backup.pak
----------------------------------------
```

The implementation maintains the packed-file count, skipped-file count, and total size. 

---

## 12. Execution

Compile the files from the `src` directory:

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

### Packing Example

```text
Enter folder name: Input
Enter packed file name: backup.pak
```

The application processes the supported files and creates:

```text
backup.pak
```

### Unpacking Example

```text
Enter packed file name: backup.pak
```

The application reads the packed file and extracts the stored files.

---

## 13. Java Concepts Used

### Core Java

* Classes and Objects
* Methods
* Variables and Data Types
* String Operations
* Exception Handling

### File Handling

* `File`
* `FileInputStream`
* `FileOutputStream`
* File paths
* File size
* File extension handling

### Collections

* `HashSet`
* `Set`

The packer uses a `HashSet` to maintain the supported file extensions. 

### Memory and Data Processing

* Byte arrays
* 1024-byte buffers
* Chunk-based processing
* XOR operation

### Resource Management

* Try-with-resources

### User Interaction

* `Scanner`
* Command-line input

---

## 14. Limitations

* Only selected file extensions are currently supported.
* The header size is fixed at 100 bytes.
* File names that exceed the available header space cannot be stored.
* XOR transformation is not suitable for secure encryption.
* The current packing process works with files directly inside the specified folder.
* Directory structures are not currently preserved.
* The application currently uses a command-line interface.
* The packed-file format does not currently include dedicated integrity verification.

---

## 15. Future Improvements

The following improvements can be added in future versions:

* Support additional file types
* Add recursive directory traversal
* Preserve directory structure
* Improve the packed-file format
* Add file integrity verification
* Add checksum/hash verification
* Add compression
* Implement secure encryption
* Add progress information
* Improve GUI support

---

## 16. Current Project Architecture

```text
                 Java File Packer-Unpacker
                          │
             ┌────────────┴────────────┐
             │                         │
        FilePacker                FileUnpacker
             │                         │
             ↓                         ↓
       Source Folder               .pak File
             │                         │
             ↓                         ↓
    Validate Files               Read Header
             │                         │
             ↓                         ↓
      Create Header              Extract Metadata
             │                         │
             ↓                         ↓
    Read 1024-byte chunks        Read Data Chunks
             │                         │
             ↓                         ↓
       XOR Transform             Reverse XOR
             │                         │
             ↓                         ↓
       Write .pak File           Restore Files
```

### Current Version Focus

The current implementation has moved toward **efficient chunk-based file processing** rather than loading complete files into memory. The packer already reads source files in 1024-byte chunks, and the unpacker has been updated to follow the same chunk-oriented approach.

This makes the project stronger from both a **Java file-handling** and **memory-management** perspective.
