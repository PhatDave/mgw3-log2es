package hr.neos.mgwlogtoes.processors;

public interface Task extends Runnable {
	void setRun(boolean run);

	void setStopDemanded(boolean stopDemanded);
}
