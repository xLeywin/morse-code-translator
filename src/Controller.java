import javax.sound.sampled.*;
import java.util.HashMap;

public class Controller {
    private HashMap<Character, String> morseCodeMap;

    public Controller() {
        morseCodeMap = new HashMap<>();

        // uppercase
        morseCodeMap.put('A',".-");
        morseCodeMap.put('B',"-...");
        morseCodeMap.put('C',"-.-.");
        morseCodeMap.put('D',"-..");
        morseCodeMap.put('E',".");
        morseCodeMap.put('F',"..-.");
        morseCodeMap.put('G',"--.");
        morseCodeMap.put('H',"....");
        morseCodeMap.put('I',"..");
        morseCodeMap.put('J',".---");
        morseCodeMap.put('K',"-.-");
        morseCodeMap.put('L',".-..");
        morseCodeMap.put('M',"--");
        morseCodeMap.put('N',"-.");
        morseCodeMap.put('O',"---");
        morseCodeMap.put('P',".--.");
        morseCodeMap.put('Q',"--.-");
        morseCodeMap.put('R',".-.");
        morseCodeMap.put('S',"...");
        morseCodeMap.put('T',"-");
        morseCodeMap.put('U',"..-");
        morseCodeMap.put('V',"...-");
        morseCodeMap.put('W',".--");
        morseCodeMap.put('X',"-..-");
        morseCodeMap.put('Y',"-.--");
        morseCodeMap.put('Z',"--..");

        // lowercase
        morseCodeMap.put('a',".-");
        morseCodeMap.put('b',"-...");
        morseCodeMap.put('c',"-.-.");
        morseCodeMap.put('d',"-..");
        morseCodeMap.put('e',".");
        morseCodeMap.put('f',"..-.");
        morseCodeMap.put('g',"--.");
        morseCodeMap.put('h',"....");
        morseCodeMap.put('i',"..");
        morseCodeMap.put('j',".---");
        morseCodeMap.put('k',"-.-");
        morseCodeMap.put('l',".-..");
        morseCodeMap.put('m',"--");
        morseCodeMap.put('n',"-.");
        morseCodeMap.put('o',"---");
        morseCodeMap.put('p',".--.");
        morseCodeMap.put('q',"--.-");
        morseCodeMap.put('r',".-.");
        morseCodeMap.put('s',"...");
        morseCodeMap.put('t',"-");
        morseCodeMap.put('u',"..-");
        morseCodeMap.put('v',"...-");
        morseCodeMap.put('w',".--");
        morseCodeMap.put('x',"-..-");
        morseCodeMap.put('y',"-.--");
        morseCodeMap.put('z',"--..");

        // numbers
        morseCodeMap.put('0',"-----");
        morseCodeMap.put('1',".----");
        morseCodeMap.put('2',"..---");
        morseCodeMap.put('3',"...--");
        morseCodeMap.put('4',"....-");
        morseCodeMap.put('5',".....");
        morseCodeMap.put('6',"-....");
        morseCodeMap.put('7',"--...");
        morseCodeMap.put('8',"---..");
        morseCodeMap.put('9',"----.");

        // special characters
        morseCodeMap.put(' ',"/");
        morseCodeMap.put(',',"--..--");
        morseCodeMap.put('.',".-.-.-");
        morseCodeMap.put('?',"..--..");
        morseCodeMap.put(';',"-.-.-.");
        morseCodeMap.put(':',"---...");
        morseCodeMap.put('(',"-.--.");
        morseCodeMap.put(')',"-.--.-");
        morseCodeMap.put('[',"-.--.");
        morseCodeMap.put(']',"-.--.-");
        morseCodeMap.put('{',"-.--.");
        morseCodeMap.put('}',"-.--.-");
        morseCodeMap.put('+',".-.-.");
        morseCodeMap.put('-',"-....-");
        morseCodeMap.put('_',"..--.-");
        morseCodeMap.put('"',".-..-.");
        morseCodeMap.put('\'',".----.");
        morseCodeMap.put('/',"-..-.");
        morseCodeMap.put('\\',"-..-.");
        morseCodeMap.put('@',".--.-.");
        morseCodeMap.put('=',"-...-");
        morseCodeMap.put('!',"-.-.--");
    }

    public String translateToMorse(String text){
        StringBuilder sb = new StringBuilder();
        for(Character letter : text.toCharArray()){
            sb.append(morseCodeMap.get(letter)).append(" ");
        }
        return sb.toString();
    }

    public void playSound(String[] morseMessage) throws LineUnavailableException, InterruptedException {
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, true);

        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        // Duration of the sounds
        int dotDuration = 200;
        int dashDuration = (int) (1.5 * dotDuration);
        int slashDuration = 2 * dashDuration;

        for(String pattern : morseMessage){
            for(char c : pattern.toCharArray()){
                if(c == '.'){
                    playBeep(sourceDataLine, dotDuration);
                    Thread.sleep(dotDuration);
                }else if(c == '-'){
                    playBeep(sourceDataLine, dashDuration);
                    Thread.sleep(dotDuration);
                }else if(c == '/'){
                    Thread.sleep(slashDuration);
                }
            }

            // Waits a bit before playing the next sequence
            Thread.sleep(dotDuration);
        }

        // Close audio output line (cleans up resources)
        sourceDataLine.drain();
        sourceDataLine.close();
        sourceDataLine.stop();
    }

    // Sends audio data to be played to the data line
    private void playBeep(SourceDataLine line, int durationMs) {

        // Sample rate must match the AudioFormat (44100 Hz)
        float sampleRate = 44100;

        // Frequency of the beep tone in Hz
        double frequency = 800.0;

        // Number of audio samples for the given duration (milliseconds)
        int numSamples = (int) (durationMs * sampleRate / 1000);

        // 16-bit audio => 2 bytes per sample (frameSize = 2)
        byte[] data = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {

            // Calculates the angle of the sine wave for the current sample
            // Uses the sample index, frequency and sample rate
            double angle = 2.0 * Math.PI * i * frequency / sampleRate;

            // Volume control: 0.0 (silence) to 1.0 (max)
            double volume = 0.15;

            // Generates the sine wave value and scales it to the range of a signed 16-bit value
            // short range: -32768 to 32767
            short sample = (short) (Math.sin(angle) * Short.MAX_VALUE * volume);

            // Splits the 16-bit sample into two bytes (big-endian)
            // High byte first, because AudioFormat is big-endian = true
            data[2 * i]     = (byte) (sample >> 8);
            data[2 * i + 1] = (byte) (sample);
        }

        // Writes the audio buffer to the sound line
        // The buffer size is now always a multiple of the frame size
        line.write(data, 0, data.length);
    }
}