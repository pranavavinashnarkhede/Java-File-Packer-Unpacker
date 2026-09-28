import java.io.*;
import java.util.*;

class FileUnpacker
{
    /*
    Function Name : unpack
    Description   : Extracts files from the packed file.
    Input         : Packed file name
    Output        : Extracted files
    */

    public void unpack(String PackedFileName) throws Exception
    {
        File fpackobj = null;

        String strHeader = null;
        File NewFile = null;

        byte Header[] = new byte[100];
        String Tokens[] = null;

        byte Buffer[] = null;
        byte transformedBuffer[] = null;

        byte XOR_KEY = 65;

        int iRet = 0;
        int i = 0;


        // Validate packed file name
        if(PackedFileName == null || PackedFileName.trim().isEmpty())
        {
            throw new Exception("Packed file name cannot be empty.");
        }


        // Validate packed file
        fpackobj = new File(PackedFileName);

        if(!fpackobj.exists())
        {
            throw new Exception("Packed file does not exist: " +PackedFileName);
        }

        if(!fpackobj.isFile())
        {
            throw new Exception("The specified path is not a packed file.");
        }


        // Open packed file and close it automatically
        try(FileInputStream fiobj = new FileInputStream(fpackobj))
        {
            /*
            Read one 100-byte header at a time.
            The header contains the file name and file size.
            */

            while(true)
            {
                int headerBytesRead = 0;


                // Read exactly 100 bytes for the header
                while(headerBytesRead < 100)
                {
                    iRet = fiobj.read(Header, headerBytesRead, 100 - headerBytesRead);

                    if(iRet == -1)
                    {
                        break;
                    }

                    headerBytesRead = headerBytesRead + iRet;
                }

                // End of packed file
                if(headerBytesRead == 0)
                {
                    break;
                }


                // Header must contain exactly 100 bytes
                if(headerBytesRead < 100)
                {
                    throw new Exception("Invalid packed file: incomplete header.");
                }


                // Convert header bytes into String
                strHeader = new String(Header);

                strHeader = strHeader.trim();

                strHeader = strHeader.replaceAll("\\s+", " ");


                // Extract file name and file size
                Tokens = strHeader.split("#");


                // Validate header format
                if(Tokens.length < 2)
                {
                    throw new Exception("Invalid packed file format.");
                }


                // Validate extracted file name
                if(Tokens[0].trim().isEmpty())
                {
                    throw new Exception( "Invalid file name in packed file.");
                }

                int fileSize;

                // Convert file size from String to integer
                try
                {
                    fileSize = Integer.parseInt(Tokens[1].trim());
                }
                catch(NumberFormatException e)
                {
                    throw new Exception("Invalid file size in packed file.");
                }

                // File size cannot be negative
                if(fileSize < 0)
                {
                    throw new Exception("Invalid file size in packed file.");
                }


                // Create the extracted file
                NewFile = new File(Tokens[0].trim());


                // Prevent accidental overwrite
                if(NewFile.exists())
                {
                    throw new Exception("File already exists: "+ NewFile.getName());
                }


                if(!NewFile.createNewFile())
                {
                    throw new Exception("Unable to create file: "+ NewFile.getName());
                }


                // Open output file and close it automatically
                try(FileOutputStream foobj = new FileOutputStream(NewFile))
                {
                    Buffer = new byte[fileSize];

                    transformedBuffer = new byte[fileSize];


                    /*
                    Read the complete file data
                    according to the size stored in the header.
                    */

                    int totalBytesRead = 0;

                    while(totalBytesRead < fileSize)
                    {
                        iRet = fiobj.read(Buffer, totalBytesRead, fileSize - totalBytesRead);

                        if(iRet == -1)
                        {
                            throw new Exception("Unexpected end of packed file "+ "while reading: "+ NewFile.getName());
                        }

                        totalBytesRead = totalBytesRead + iRet;
                    }


                    /*
                    Reverse the XOR transformation
                    applied during packing.
                    */

                    for(i = 0; i < fileSize; i++)
                    {
                        transformedBuffer[i] = (byte)(Buffer[i] ^ XOR_KEY);
                    }


                    // Write the restored data to the extracted file
                    foobj.write(transformedBuffer, 0, fileSize);

                } // Output stream automatically closed here

            }
        } // Input stream automatically closed here
    }


    /*
    Function Name : main
    Description   : Takes packed file name from the user
                    and starts the unpacking process.
    */

    public static void main(String A[]) throws Exception
    {
        Scanner sobj = new Scanner(System.in);


        System.out.println("----------------------------------------");

        System.out.println("-----------Java File Unpacker-----------");

        System.out.println("----------------------------------------");


        System.out.print("Enter packed file name: ");

        String packedFileName = sobj.nextLine().trim();

        try
        {
            FileUnpacker uObj =new FileUnpacker();

            uObj.unpack(packedFileName);

            System.out.println();

            System.out.println("Unpacking completed successfully!");
        }
        catch(Exception e)
        {
            System.out.println();

            System.out.println("Unpacking failed: "+ e.getMessage());
        }

        sobj.close();
    }
}

