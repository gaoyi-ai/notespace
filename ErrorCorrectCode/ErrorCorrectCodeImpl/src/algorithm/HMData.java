package algorithm;

import java.util.List;

public class HMData extends Data {
    public HMData(int size, ParityType parityType) {
        super(size, parityType);
    }

    public List<Integer> getDataWithParity() {
        return dataWithParity;
    }

    public void setDataWithParity(List<Integer> dataWithParity) {
        this.dataWithParity = dataWithParity;
    }

    private List<Integer> dataWithParity;

    @Override
    public String toString() {
        return "HMData{" + "isCorrect="+super.isCorrect() +
                ", dataWithParity=" + dataWithParity +
                '}';
    }
}
