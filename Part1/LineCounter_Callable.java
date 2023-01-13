package Part1;

import java.util.concurrent.Callable;

public class LineCounter_Callable implements Callable {
    String file_name;

    public LineCounter_Callable (String file_name) {
        this.file_name = file_name;
    }

    public Integer call() throws Exception
    {
        return Ex2_1.getNumOfLines(this.file_name);
    }
}