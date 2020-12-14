import org.junit.*;
import java.io.FileReader;

public class SampleTest {
	@Test
	public void run () {
		try {
			FileReader rdr = new FileReader ("src/test/resources/curfew_structs_minimal.txt");
		} catch (Exception e) {
			System.out.println (e.getMessage());
			assert false;
		}
	}
}
