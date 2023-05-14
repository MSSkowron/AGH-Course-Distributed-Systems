package sr.ice.client;

import java.lang.Exception;

import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;
public class IceClient
{
	public static void runCommand(ObjectPrx base, String command, byte[] inParams) {
		try {
			Object.Ice_invokeResult r = base.ice_invoke(command, OperationMode.Normal, inParams);
			if(r.returnValue) {
				InputStream is = new InputStream(r.outParams);
				switch (command){
					case "add":
					case "subtract":
						is.startEncapsulation();
						var longResult = is.readLong();
						is.endEncapsulation();
						System.out.println(command.toUpperCase() + " RESULT: " + longResult);
						break;
					case "avg":
						is.startEncapsulation();
						var avgLongResult = is.readFloat();
						is.endEncapsulation();
						System.out.println("AVG RESULT: " + avgLongResult);
						break;
					case "op":
						System.out.println("OP RESULT: " + "void");
						break;
					default:
						System.out.println("UNKNOWN COMMAND TYPE");
				}
			} else {
				System.out.println("No result while calling " + command);
			}
		} catch(com.zeroc.Ice.LocalException ex) {
			System.err.println("Error while calling " + command + ": " + ex.getMessage());
		}
	}

	public static void main(String[] args)
	{
		int status = 0;
		Communicator communicator = null;

		try {
			// Inicjalizacja komunikatora Ice
			communicator = Util.initialize(args);

			// Uzyskanie referencji do zdalnego obiektu 'calc'
			ObjectPrx base = communicator.stringToProxy("calc:tcp -h localhost -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");

			// Wywolanie operacji add
			int a = 3, b = 4;
			OutputStream outAdd = new OutputStream(communicator); // Obiekt za pomoca ktorego serializujemy nasze dane i zapisujemy je w strumieniu wyjsciowym
			outAdd.startEncapsulation(); // Rozpoczecie procesu serializacji
			outAdd.writeInt(a);
			outAdd.writeInt(b);
			outAdd.endEncapsulation(); // Zakonczenie procesu serializacji
			byte[] inParamsAdd = outAdd.finished(); // Tablica bajtow, ktora zawiera zserializowane dane
			runCommand(base, "add", inParamsAdd);

			// Wywolanie operacji subtract
			OutputStream outSubtract = new OutputStream(communicator);
			outSubtract.startEncapsulation();
			outSubtract.writeInt(a);
			outSubtract.writeInt(b);
			outSubtract.endEncapsulation();
			byte[] inParamsSubtract = outSubtract.finished();
			runCommand(base, "subtract", inParamsSubtract);

			// Wywolanie operacji avg
			long[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			OutputStream outAvg = new OutputStream(communicator);
			outAvg.startEncapsulation();
			outAvg.writeLongSeq(numbers);
			outAvg.endEncapsulation();
			byte[] inParamsAvg = outAvg.finished();
			runCommand(base, "avg", inParamsAvg);

			// Wywolanie operacji op
			OutputStream out3 = new OutputStream(communicator);
			out3.startEncapsulation();
			short a1_a = 10;
			out3.writeShort(a1_a);
			long a1_b = 20;
			out3.writeLong(a1_b);
			float a1_c = 30;
			out3.writeFloat(a1_c);
			String a1_d = "de-de-de";
			out3.writeString(a1_d);
			short b1 = 50;
			out3.writeShort(b1);
			out3.endEncapsulation();
			byte[] inParams3 = out3.finished();
			runCommand(base, "op", inParams3);
		} catch (LocalException e) {
			System.err.println("Error while calling command: " + e.getMessage());
			status = 1;
		} catch (Exception e) {
			System.err.println("Error while calling command: " + e.getMessage());
			status = 2;
		} finally {
			if (communicator != null) {
				try {
					communicator.destroy();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					status = 3;
				}
			}

			System.exit(status);
		}
	}
}