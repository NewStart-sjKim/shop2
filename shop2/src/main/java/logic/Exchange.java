package logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {
	private int eno;
	private String code;
	private String name;
	private float primeamt;
	private float sellamt;
	private float buyamt;
	private String edate;
	
}
