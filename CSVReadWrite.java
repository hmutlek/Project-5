/**
 * This reads a csv file
 * Currently meant to be used as a template, right now it just prints the lines
 * waiting for more work to be done so it can read into needed fields.
 * This class has a few methods, read, getLines, and append
 *
 * <p>Purdue University -- CS18000 -- Summer 2021</p>
 *
 * @author Paul Holley paulh
 * @version 7/18/21 about 7:50
 */
import java.io.*;
import java.util.ArrayList;

public class CSVReadWrite {
    public String fileName;
    public ArrayList<String> lines = new ArrayList<String>();

    //constructs the object and then reads the file.
    public CSVReadWrite(String fileName) {
        this.fileName = fileName;
    }

    //reads the file and updates the lines field
    public ArrayList<String> readFile() throws FileNotFoundException {
        this.lines.clear();
        try {
            ArrayList<String> temp = new ArrayList<String>();
            File file = new File(this.fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            //add each line to a list
            line = br.readLine();

            if (line != null) {
                do {
                    this.lines.add(line);
                } while ((line = br.readLine()) != null);
                br.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return this.lines;
    }

    // get the lines from the file that have been read, use read() first to read the file.
    public ArrayList<String> getLines() {
        return this.lines;
    }

    //the string toAppend make sure the values are separated by commas or it won't work
    //handles the index part so nobody has to worry about that part.
    public void append(String toAppend) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName, true))) {
            String index = String.valueOf(Integer.parseInt(this.lines.get(this.lines.size() - 1).split(",")[0]) + 1) + ",";
            writer.newLine();
            writer.write(index + toAppend);
            writer.flush();
        }
    }

    //same append function but the index is not done automatically if needed.
    public void indexAppend(String toAppend) throws IOException {
        try (FileWriter writer = new FileWriter(this.fileName, true)) {
            writer.write(toAppend + "\n");
        }
    }


    //Writes all lines in object.lines to the file they are part of
    public void writeLines() throws IOException {
        try (FileWriter writer = new FileWriter(this.fileName, false)) {
            for (String line : this.lines) {
                writer.write(line + "\n");
            }
        }
    }


    //three inputs index of the identifier being use, the identifier that is used, and the string I want to write to file
    //once it finds the first instance of the identifier it stops looking and work on that.
    //handles the index part of this so that is not something that needs to be worried about.
    public void replaceLine(int identifierIndex, String identifier, String replacement) throws IOException {
        //go through lines to find where it is equal
        int counter = 0;
        for (String line : this.lines) {
            if (line.split(",")[identifierIndex].equals(identifier)) {
                //gets index of line being replaced and takes that index spot now
                String index = line.split(",")[0];
                //if it is equals it goes to the lines list and replaces it
                this.lines.set(counter, index + "," + replacement);
                // rewrites all lines
                this.writeLines();
                break;
            }
            counter++;
        }
    }

    //same replace line method but without the index done automatically
    public void indexReplaceLine(int identifierIndex, String identifier, String replacement) throws IOException {
        int counter = 0;
        for (String line : this.lines) {
            if (line.split(",")[identifierIndex].equals(identifier)) {
                //if it is equals it goes to the lines list and replaces it
                this.lines.set(counter, replacement);
                // rewrites all lines
                this.writeLines();
                break;
            }
            counter++;
        }
    }

    public void removeLine(int identifierIndex, String identifier) throws IOException {
        int counter = 0;
        for (String line : this.lines) {
            if (line.split(",")[identifierIndex].equals(identifier)) {
                this.lines.remove(counter);
                this.writeLines();
                break;
            }
            counter++;
        }
    }

    //returns an String of what the next index value is going to be
    public String getNextIndex() {
        return String.valueOf(Integer.parseInt(this.lines.get(this.lines.size() - 1).split(",")[0]) + 1);
    }


    //main exists to show off the example
    public static void main(String[] args) throws IOException {
        // if the csv is in the project folder then it should be able to read it with just the name of the file
        CSVReadWrite test = new CSVReadWrite("TestFile.csv");
        //once the object is created you need to read the file to update it
        test.readFile();

        //replaces the line where it has mac with dee, handles the index by itself
        test.replaceLine(1,"mac","dee");
        //appends bubbles to the  csv and handles the index part
        System.out.println(test.getNextIndex());
        test.readFile();
        for (String line : test.getLines()) {
            System.out.println(line);
        }
    }
}
