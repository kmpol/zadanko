package pl.karol;

import java.io.Serializable;

public class FileCounter implements Serializable {

    private Integer numberOfFilesMoved = 0;
    private Integer numberOfFilesInTest = 0;
    private Integer numberOfFilesInDev = 0;

    public Integer getNumberOfFilesMoved() {
        return numberOfFilesMoved;
    }

    public void setNumberOfFilesMoved(Integer numberOfFilesMoved) {
        this.numberOfFilesMoved = numberOfFilesMoved;
    }

    public Integer getNumberOfFilesInTest() {
        return numberOfFilesInTest;
    }

    public void setNumberOfFilesInTest(Integer numberOfFilesInTest) {
        this.numberOfFilesInTest = numberOfFilesInTest;
    }

    public Integer getNumberOfFilesInDev() {
        return numberOfFilesInDev;
    }

    public void setNumberOfFilesInDev(Integer numberOfFilesInDev) {
        this.numberOfFilesInDev = numberOfFilesInDev;
    }
}
