package ch.riccardo.patane.hybridStreams.model;

public class FableData {

    private Fable fable;
    private int offset, length;

    public FableData(Fable fable, int offset, int length) {
        this.fable = fable;
        this.offset = offset;
        this.length = length;
    }

    public Fable getFable() {
        return fable;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "FableData{" +
                "fable=" + fable +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
