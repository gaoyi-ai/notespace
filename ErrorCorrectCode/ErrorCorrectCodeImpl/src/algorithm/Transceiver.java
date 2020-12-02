package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Transceiver implements Parity {
    protected Data data;

    @Override
    public int evenOddParity() {
        int parity = data.getParityType().ordinal();
        for (int i = 0; i < data.getSize(); i++) {
            parity ^= data.getData().get(i);
        }
        return parity;
    }

    private static int getHMParitySize(int dataSize) {
        return IntStream.range(3, dataSize).filter(x -> (x + dataSize + 1) <= (1 << x)).findFirst().getAsInt(); // absolutely exist
    }

    protected static int getHMParity(int[] data, int defaultParity, int hmParityIdx) {
        int parity = defaultParity;

        for (int i = 0; i < data.length; i++) {
            int hmIdx = i + 1;
            int destIdx = hmIdx & hmParityIdx;
            if ((i + 1 & i) != 0 && destIdx >= hmParityIdx) { // data bit to parity
                parity ^= data[i];
            }
        }
        return parity;
    }

    private static List<Integer> getHMDataWithParity(HMData data, int paritySize) {
        int[] parities = new int[paritySize];
        int[] d = new int[data.getSize() + paritySize];
        // fill d with data
        for (int i = 0, dataIdx = 0; i < d.length; i++) {
            if (((i + 1) & i) != 0) { // data bit
                d[i] = data.getData().get(dataIdx++);
            }
        }
        // compute parity
        int parityIdx = 0, i = 0;
        do {
            int parity = getHMParity(d, 0, parityIdx + 1);
            parities[i] = parity;
            d[parityIdx] = parity;
            parityIdx = (1 << ++i) - 1;
        } while (parityIdx < d.length);
        data.setParity(Arrays.stream(parities).boxed().collect(Collectors.toList()));
        return Arrays.stream(d).boxed().collect(Collectors.toList());
    }

    @Override
    public List<Integer> HMParity() {
        HMData hmData = (HMData) data;
        int size = hmData.getSize();
        int hmParitySize = getHMParitySize(size);
        return getHMDataWithParity(hmData, hmParitySize);
    }

    @Override
    public List<Integer> CRCParity(List<Integer> polynomial) {
        int polynomialSize = polynomial.size();
        List<Integer> divisor = new ArrayList<>(data.getData());
        // generate divisor
        if (data.getParity() == null) {
            for (int i = 0; i < polynomialSize - 1; i++)
                divisor.add(0);
        } else divisor.addAll(data.getParity());
        int divisorSize = divisor.size();
        // generate xor
        List<Integer> xor = new ArrayList<>(data.getData().subList(0, polynomialSize));
        int xorSize = xor.size();
        // cur: current xor end in divisor
        // fillBit: first index of 1
        int cur = polynomialSize, fillBit = 0;
        while (cur < divisorSize + 1) {
            // prepare new xor
            List<Integer> tmp = xor.subList(fillBit, xorSize);// from first index of 1
            if (tmp.size() < xorSize) tmp.addAll(divisor.subList(cur - fillBit, cur));
            xor = tmp;
            // handle xor
            for (int j = 0; j < polynomialSize; j++) {
                int xorRes = xor.get(j) ^ polynomial.get(j);
                xor.set(j, xorRes);
            }
            // get result
            fillBit = xor.indexOf(1);
            if (fillBit == -1)
                if (divisor.subList(cur, divisorSize).size() < polynomialSize)
                    break; // all 0 in xor && rest size < polynomialSize
                else {
                    // still have more than polynomialSize bits
                    xor = divisor.subList(cur, cur + xorSize);
                    cur += polynomialSize;
                    fillBit = 0;
                }
            cur += fillBit;
        }
        if (cur < divisorSize && fillBit == -1) {
            // case: last xor get all 0 && remain divisor size < polynomialSize
            List<Integer> remain = divisor.subList(cur, divisorSize);
            while (remain.size() < xorSize - 1)// fill front
                remain.add(0, 0);
            return remain;
        } else if (cur == divisorSize && fillBit == -1) {
            // case: just remain divisor has no remain
            return xor.subList(1, xorSize);
        } else {
            // case: cur > divisorSize && no possibility xor all 0
            List<Integer> tmp = xor.subList(fillBit, xorSize);// from first index of 1
            if (tmp.size() < xorSize)// fill back
                tmp.addAll(divisor.subList(cur - fillBit, divisorSize));
            while (tmp.size() < xorSize)// fill front
                tmp.add(0, 0);
            xor = tmp;
            return xor.subList(1, xorSize);
        }
    }


}
