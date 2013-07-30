package de.druz.game.gdxgame.components;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Component;

public class DebugOutput extends Component implements Outputable {

	public List<Outputable> out = new ArrayList<Outputable>();
	
	public DebugOutput(Outputable ... outputters) {
		for (Outputable outputable : outputters) {
			this.out.add(outputable);
		}
	}

	@Override
	public String output() {
		StringBuilder result = new StringBuilder();
		for (Outputable output : out) {
			result.append(output.output()).append(" - ");
		}
		return result.toString();
	}
	
}
