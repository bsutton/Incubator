package net.sf.incubator.organisms.brain;

import java.util.Vector;

import org.joone.engine.InputPatternListener;
import org.joone.engine.Layer;
import org.joone.engine.OutputPatternListener;
import org.joone.engine.Synapse;

public class LayerStub
{
	BrainGestator gestator;
	Layer layer;

	int layerType;
	short rows;

	Vector<Synapse> outputSynapse = new Vector<Synapse>();
	Vector<Synapse> inputSynapse = new Vector<Synapse>();

	public LayerStub(BrainGestator gestator, int layerType, short rows)
	{
		this.gestator = gestator;
		this.layerType = layerType;
		this.rows = rows;
	}

	public Layer create() throws InstantiationException, IllegalAccessException
	{
		layer = gestator.layerFactory(layerType, rows);

		for (Synapse synapse : outputSynapse)
			layer.addOutputSynapse(synapse);

		for (Synapse synapse : inputSynapse)
			layer.addInputSynapse(synapse);

		return layer;

	}

	public int getInputCount()
	{
		return inputSynapse.size();
	}

	public int getOutputCount()
	{
		return outputSynapse.size();
	}

	public void addOutputSynapse(Synapse synapse)
	{
		outputSynapse.add(synapse);

	}

	public void addInputSynapse(Synapse synapse)
	{
		inputSynapse.add(synapse);

	}

	public String toString()
	{
		@SuppressWarnings("unchecked")
		Vector<OutputPatternListener> output = layer.getAllOutputs();
		@SuppressWarnings("unchecked")
		Vector<InputPatternListener> input = layer.getAllInputs();

		return "Layer: Type (" + layerType + ") Inputs("
				+ (input == null ? 0 : input.size()) + ") Outputs ("
				+ (output == null ? 0 : output.size()) + ") Rows("
				+ layer.getRows() + ")";
	}

}
