package click.yinsb.icmtracing.temporal.model;

public enum WorkflowResult {
	PASS,
	FAIL,;

	public static WorkflowResult fromName(String name) {
		for (WorkflowResult a : values()) {
			if (a.toString().equals(name)) {
				return a;
			}
		}
		return null;
	}
}