package csc435.app;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class CleanDataset
{
    public long dataset_size = 0;
    public double execution_time = 0.0;

    public void clean_dataset(String inputDir, String outputDir)
    {
        long startTime = System.currentTimeMillis();
        try {
            Files.createDirectories(Paths.get( outputDir));
            processDirectory(new File(inputDir), inputDir, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        execution_time = endTime - startTime;
    }

    private void processDirectory(File dir, String inputDir, String outputDir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processDirectory(file, inputDir, outputDir);
                } else {
                    processFile(file, inputDir, outputDir);
                }
            }
        }
    }

    private void processFile(File file, String inputDir, String outputDir) throws IOException {
        // Read file
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String content = new String(fileBytes, StandardCharsets.UTF_8);

        //dataset size
        dataset_size += fileBytes.length;

        //cleaning data
        content = content.replaceAll("[^a-zA-Z0-9\\s]", ""); // Remove special characters
        content = content.replaceAll("\\s{2,}", "\n"); // Replace multiple whitespaces

        //output file path.
        String relativePath = file.getAbsolutePath().substring(inputDir.length());
        File outputFile = new File(outputDir, relativePath);

        // Ensuring the output directory exists
        outputFile.getParentFile().mkdirs();

        // Write the cleaned data
        Files.write(outputFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.err.println("improper number of arguments");
            System.exit(1);
        }

        CleanDataset cleanDataset = new CleanDataset();

        cleanDataset.clean_dataset(args[0], args[1]);
        
        System.out.print("Finished cleaning " + cleanDataset.dataset_size + " bytes of data");
        System.out.println(" in " + cleanDataset.execution_time + " milliseconds");
    }
}
