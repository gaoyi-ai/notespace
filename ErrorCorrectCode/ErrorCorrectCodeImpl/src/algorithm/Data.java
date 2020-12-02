package algorithm;

import java.util.List;

public class Data {
    private List<Integer> data;
    private final int size;
    private List<Integer> parity;
    private final ParityType parityType;
    private boolean isCorrect = true;


    public Data(int size, ParityType parityType) {
        this.size = size;
        this.parityType = parityType;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public List<Integer> getParity() {
        return parity;
    }

    public void setParity(List<Integer> parity) {
        this.parity = parity;
    }

    public int getSize() {
        return size;
    }

    public ParityType getParityType() {
        return parityType;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Data{" +
                "data=" + data +
                ", size=" + size +
                ", parity=" + parity +
                ", parityType=" + parityType +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
