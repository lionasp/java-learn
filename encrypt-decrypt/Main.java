import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

abstract class DecoderEncoder {
    protected String data;
    protected int shift;
    protected String mode;

    DecoderEncoder(String data, int shift, String mode) {
        this.data = data;
        this.shift = shift;
        this.mode = mode;
    }

    abstract public String handle();
}

class UnicodeDecoderEncoder extends DecoderEncoder {
    public UnicodeDecoderEncoder(String data, int shift, String mode) {
        super(data, shift, mode);
    }

    @Override
    public String handle() {
        int corrector = mode.equals("enc") ? 1 : -1;
        char[] result = new char[data.length()];

        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            int code = (int) ch + corrector * shift;
            result[i] = (char) code;
        }
        return new String(result);
    }
}


class ShiftDecoderEncoder extends DecoderEncoder {
    final int firstLetterNumber = (int) 'a';
    final int lastLetterNumber = (int) 'z';

    public ShiftDecoderEncoder(String data, int shift, String mode) {
        super(data, shift, mode);
    }

    @Override
    public String handle() {
        char[] result = new char[data.length()];
        int corrector = mode.equals("enc") ? 1 : -1;

        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            int code = ch;

            if (firstLetterNumber <= code && code <= lastLetterNumber) {
                code += corrector * shift;
                if (code > lastLetterNumber) {
                    code = firstLetterNumber + code % lastLetterNumber - 1;
                }
                if (code < firstLetterNumber) {
                    code = lastLetterNumber - (firstLetterNumber - code) + 1;
                }
                result[i] = (char) code;
            } else {
                result[i] = ch;
            }
        }
        return new String(result);
    }
}


class Algorithm {
    public static DecoderEncoder createAlgorithm(String type, int shift, String data, String mode) {
        switch (type) {
            case "unicode":
                return new UnicodeDecoderEncoder(data, shift, mode);
            default:
                return new ShiftDecoderEncoder(data, shift, mode);
        }
    }


}


public class Main {

    public static void main(String[] args) {
        String mode = "enc";
        String message = "";
        String algorithm = "shift";
        int shift = 0;
        boolean error = false;
        File out = null;

        for (int i = 0; i < args.length && !error; i++) {
            switch (args[i]) {
                case "-mode":
                    mode = args[++i];
                    break;
                case "-key":
                    shift = Integer.parseInt(args[++i]);
                    break;
                case "-data":
                    message = args[++i];
                    break;
                case "-alg":
                    algorithm = args[++i];
                    break;
                case "-in":
                    File file = new File(args[++i]);
                    try (Scanner scanner = new Scanner(file)) {
                        message = scanner.nextLine();
                    } catch (FileNotFoundException e) {
                        error = true;
                    }
                    break;
                case "-out":
                    out = new File(args[++i]);
                    break;
                default:
                    break;
            }
        }

        if (error) {
            System.out.println("Error");
        } else {
            String result = Algorithm.createAlgorithm(algorithm, shift, message, mode).handle();

            if (out == null) {
                System.out.println(result);
            } else {
                try (FileWriter writer = new FileWriter(out)) {
                    writer.write(result);
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }
        }
    }
}