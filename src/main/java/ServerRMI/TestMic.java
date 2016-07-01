package ServerRMI;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class TestMic {

	public static void main(String[] args) {
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
		TargetDataLine microphone;
		SourceDataLine speakers;
		try {
			microphone = AudioSystem.getTargetDataLine(format);

			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			microphone = (TargetDataLine) AudioSystem.getLine(info);
			microphone.open(format);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int numBytesRead;
			int CHUNK_SIZE = 1024;
			byte[] data = new byte[microphone.getBufferSize() / 5];
			microphone.start();

			int bytesRead = 0;
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			speakers.open(format);
			speakers.start();
			while (bytesRead < 100000) {
				numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
				bytesRead += numBytesRead;
				// write the mic data to a stream for use later
				out.write(data, 0, numBytesRead);
				// write mic data to stream for immediate playback
				speakers.write(data, 0, numBytesRead);
			}
			speakers.drain();
			speakers.close();
			microphone.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

}