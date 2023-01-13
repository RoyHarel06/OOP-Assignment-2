package Part1;

import java.io.FileNotFoundException;

public class LineCounter_Threads extends Thread{
    String file_name;
    int number_of_lines;

    public LineCounter_Threads(String file_name) {
        this.file_name = file_name;
        this.number_of_lines = -1;
    }

    public void run() {
        try {
            this.number_of_lines = Ex2_1.getNumOfLines(this.file_name);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfLines() {
        return this.number_of_lines;
    }
}
