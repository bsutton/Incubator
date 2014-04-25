/*
 * Created on 11/05/2005
 * 
 * The job of the BrainGestator is to take a DNA strand and interpret it as a
 * series of neural layers and connections.
 * 
 * At the completion of the Gestation a brain should exist and dependant on the
 * DNA it may even function :)
 */
package net.sf.incubator.organisms.brain;

import java.util.Arrays;
import java.util.Vector;

import net.sf.incubator.DNAStrand;
import net.sf.incubator.DNAStrandIterator;

import org.joone.engine.ContextLayer;
import org.joone.engine.DelayLayer;
import org.joone.engine.DelaySynapse;
import org.joone.engine.DirectSynapse;
import org.joone.engine.FullSynapse;
import org.joone.engine.GaussianLayer;
import org.joone.engine.KohonenSynapse;
import org.joone.engine.Layer;
import org.joone.engine.LinearLayer;
import org.joone.engine.LogarithmicLayer;
import org.joone.engine.SangerSynapse;
import org.joone.engine.SigmoidLayer;
import org.joone.engine.SineLayer;
import org.joone.engine.Synapse;
import org.joone.engine.TanhLayer;
import org.joone.engine.WTALayer;
import org.joone.engine.learning.TeacherSynapse;

/**
 * @author bsutton
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class BrainGestator
{

	// Define the support neural layer types

	private static final int MAX_MAP_VALUE = 0x16;

	static final int LAYER_LINEAR = 0x01;

	static final int LAYER_SIGMOID = 0x02;

	static final int LAYER_TANH = 0x03;

	static final int LAYER_LOGARITHMIC = 0x4;

	static final int LAYER_DELAY = 0x5;

	static final int LAYER_SINE = 0x6;

	static final int LAYER_CONTEXT = 0x7;

	static final int LAYER_WINNER_TAKE_ALL = 0x8;

	static final int LAYER_GAUSSIAN = 0x9;

	static final int SYNAPSE_DIRECT = 0xA;

	static final int SYNAPSE_FULL = 0xB;

	static final int SYNAPSE_DELAY = 0xC;

	static final int SYNAPSE_KOHONEN = 0xD;

	static final int SYNAPSE_SANGER = 0xE;

	// Special input synapse there may only be one!
	// Used to pass the input data into the brain.
	static final int SYNAPSE_INPUT = 0xF;

	// Special input synapse there may only be one!
	// Used to pass the input data into the brain.
	static final int SYNAPSE_OUTPUT = 0x10;

	static final int MAX_ROWS = 100;

	MyInputSynapse inputSynapse;

	TeacherSynapse teacherSynapse;

	int neurons = 0;

	int synapes = 0;

	int witheredSynapes = 0;

	int junk = 0;

	Vector<NeuralLayerMap<? extends Layer>> mapNeuralLayers = new Vector<NeuralLayerMap<? extends Layer>>();

	/**
	 * This method decodes the strand of dna by serially reading the sequence
	 * looking for recognizable elements.
	 * 
	 * Certain portions of the structure of the dna are well defined where as
	 * other section may contain junk.
	 * 
	 * Each neuron is followed by a short which dictates the number of rows in
	 * the neuron (or more acurately the number of neurons in the neural layer).
	 * 
	 * 
	 * @param strand
	 * @param testCases
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws StillBirthException 
	 */
	public Brain gestate(DNAStrand strand, TestCases testCases) throws InstantiationException, IllegalAccessException, StillBirthException
	{
		Brain brain = new Brain(strand);
		inputSynapse = new MyInputSynapse(testCases);
		teacherSynapse = new TeacherSynapse();

		int[] neuralLayerIndex = new int[strand.length() * 2];
		int[] synapseIndex = new int[strand.length() * 2];

		initNeuralMap();

		DNAStrandIterator iter = new DNAStrandIterator(strand);

		// Extract global settings from the start of the dna sequence.
		float learningRate = iter.getValueAsFloat();
		brain.setLearningRate(learningRate);
		float momentum = iter.getValueAsFloat();
		brain.setMomentum(momentum);
		short cycles = iter.getValue();
		brain.setCycles(cycles);

		// Start by finding all of the neurons and synapes
		while (iter.hasNext())
		{
			short value = iter.getValue();

			// see if its a layer
			if (isNeuralLayer(value))
			{
				neuralLayerIndex[neurons] = iter.position();
				// skip of the next value which will be used to dictate
				// the no. of rows in the layer
				if (iter.hasNext())
					iter.next();
				neurons++;
			}
			else
			{
				if (isSynapse(value))
				{
					// After each synapse the next two locations
					// are used to indicated the neurons that the
					// synapes connects to.
					// So:
					// skip over the left and right neural indices
					int position = iter.position();
					if (iter.hasNext())
						iter.next();
					if (iter.hasNext())
					{
						iter.next();
						synapseIndex[synapes] = position;
						synapes++;
					}
				}
				else
					junk++;
			}
		}

		build(brain, synapseIndex, neuralLayerIndex, strand);
		brain.setInputSynapse(inputSynapse);
		// brain.setOutputSynapse(outputSynapse);

		brain.initialise();
		return brain;
	}

	private void initNeuralMap()
	{
		mapNeuralLayers.add(new NeuralLayerMap<LinearLayer>(LAYER_LINEAR, LinearLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<SigmoidLayer>(LAYER_SIGMOID, SigmoidLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<TanhLayer>(LAYER_TANH, TanhLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<LogarithmicLayer>(LAYER_LOGARITHMIC, LogarithmicLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<DelayLayer>(LAYER_DELAY, DelayLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<SineLayer>(LAYER_SINE, SineLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<ContextLayer>(LAYER_CONTEXT, ContextLayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<WTALayer>(LAYER_WINNER_TAKE_ALL, WTALayer.class));
		mapNeuralLayers.add(new NeuralLayerMap<GaussianLayer>(LAYER_GAUSSIAN, GaussianLayer.class));

	}

	/**
	 * Constructs a brain by iterating through all of the possible synapse and
	 * provided that both ends are connected both the synapes and both
	 * connection neurons are created and added to the brain.
	 * 
	 * @param brain
	 * @param synapseIndex
	 *            - a list of indices into the dna where a synapse can be found.
	 * @param neuralLayerIndex
	 *            = a list of indices into the dna where a neuron can be found.
	 * @param strand
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws StillBirthException 
	 */
	public void build(Brain brain, int[] synapseIndex, int[] neuralLayerIndex, DNAStrand strand)
			throws InstantiationException, IllegalAccessException, StillBirthException
	{
		System.out.println("Building brain " + brain);

		// We need to track each neuron against its location in the dna as we
		// construct them
		LayerStub[] neuralLayer = new LayerStub[strand.asIntArray().length * 2];

		int[] dna = strand.asIntArray();

		for (int index = 0; index < synapseIndex.length; index++)
		{
			if (synapseIndex[index] != 0)
			{
				// We have a valid synapse

				// Start by checking that both the left and right ends of the
				// synapes are connected to valid neurons
				// otherwise just let the synapse wither
				int synapseOffset = synapseIndex[index];
				int synapseType = getValue(dna, synapseOffset) % MAX_MAP_VALUE;

				// neuron locations can't be off the end of the dna.
				if (synapseOffset >= synapseIndex.length - 2)
					continue;

				// we take the modulus so that all locations are guarenteed to
				// be within
				// the strand. This increases the likely hood of getting a valid
				// connection.
				int leftNeuronLocation = getValue(dna, synapseOffset + 1) % (dna.length * 2);
				int rightNeuronLocation = getValue(dna, synapseOffset + 2) % (dna.length * 2);

				if (leftNeuronLocation >= 0 && (leftNeuronLocation + 1) < dna.length * 2 && rightNeuronLocation >= 0
						&& (rightNeuronLocation + 1) < dna.length * 2)
				{
					int leftNeuralType = getValue(dna, leftNeuronLocation) % MAX_MAP_VALUE;
					int rightNeuralType = getValue(dna, rightNeuronLocation) % MAX_MAP_VALUE;

					// Check that a neuron exists at the given locations

					// first left
					if (Arrays.binarySearch(neuralLayerIndex, leftNeuronLocation) == -1)
						continue;

					// then right
					if (Arrays.binarySearch(neuralLayerIndex, rightNeuronLocation) == -1)
						continue;

					// Now check that the left and right target neurons are
					// valid neuron types
					if (!isNeuralLayer(leftNeuralType))
						continue;

					if (!isNeuralLayer(rightNeuralType))
						continue;

					// Looks like a fully connected synapes so construct
					// everything
					Synapse synapse = synapseFactory(synapseType);
					if (synapse != null) // The synapseType may not be valid.
					{

						// See if the left layer needs to be created.
						if (neuralLayer[leftNeuronLocation] == null)
						{
							neuralLayer[leftNeuronLocation] = new LayerStub(this, getValue(dna, leftNeuronLocation),
									getValue(dna, leftNeuronLocation + 1));

							// neuralLayer[leftNeuronLocation] = layerFactory(
							// getValue(dna, leftNeuronLocation), getValue(
							// dna, leftNeuronLocation + 1));
							// brain.addLayer(neuralLayer[leftNeuronLocation]);
						}
						// else
						// neuralLayer[leftNeuronLocation].incOutputCount();

						// See if the right layer beeds to be created.
						if (neuralLayer[rightNeuronLocation] == null)
						{
							neuralLayer[rightNeuronLocation] = new LayerStub(this, getValue(dna, rightNeuronLocation),
									getValue(dna, rightNeuronLocation + 1));

							// neuralLayer[rightNeuronLocation] = layerFactory(
							// getValue(dna, rightNeuronLocation), getValue(
							// dna, rightNeuronLocation + 1));
							// brain.addLayer(neuralLayer[rightNeuronLocation]);
						}

						neuralLayer[leftNeuronLocation].addOutputSynapse(synapse);
						neuralLayer[rightNeuronLocation].addInputSynapse(synapse);

					}
				}
				else
					witheredSynapes++;
			}
		}

		// Now see what layerstubs actually need to be expressed.
		int count = 0;
		for (LayerStub stub : neuralLayer)
		{
			if (stub != null && stub.getInputCount() > 0 && stub.getOutputCount() > 0)
			{
				count++;
				Layer layer = stub.create();

				System.out.println("Adding Layer " + layer.toString());
				// + neuralLayerDetails(neuralLayer[leftNeuronLocation],
				// leftNeuronLocation)
				// + "] right Neural Layer [" +
				// neuralLayerDetails(neuralLayer[rightNeuronLocation],
				// rightNeuronLocation)
				// + "] Synapse " + synapse);

				brain.addLayer(layer);
			}
		}

		if (count == 0)
			throw new StillBirthException("Brain delivered with zero Layers");
		System.out.println("Brain delivered with " + count + " layers.");
	}

	// String neuralLayerDetails(LayerStub layer, int location)
	// {
	// Vector output = layer.getAllOutputs();
	// Vector input = layer.getAllInputs();
	//
	// return "Layer(" + location + ") Inputs(" + (input == null ? 0 :
	// input.size())
	// + ") Outputs (" + (output == null ? 0 : output.size())
	// + ") Rows(" + layer.getRows() + ")";
	// }

	/**
	 * Returns the value stored in a dna strand at the given index. The strand
	 * is an array of integers but we process it as a sequence of shorts.
	 * 
	 * @param dna
	 * @param index
	 * @return
	 */
	private short getValue(int[] dna, int index)
	{
		short value = 0;

		// Determine if we need the upper or lower short.
		switch (index % 2)
		{
			// upper short
			case 0:
				value = (short) ((dna[index / 2] >> 16) % (Short.MAX_VALUE));
				break;
			// lower short
			case 1:
				value = (short) (dna[index / 2] % (Short.MAX_VALUE));
				break;
		}
		return value;
	}

	/**
	 * Returns the value stored in a dna strand at the given index as a float
	 * Unlike getValue this method uses the full int and can only operate at
	 * even boundary points (e.g. index must be divisible by two with no
	 * remander).
	 * 
	 * 
	 * @param dna
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unused")
	private float getValueAsFloat(int[] dna, int index)
	{
		if (index % 2 != 0)
			throw new IllegalArgumentException("index must be divisible by two");

		float value = Float.intBitsToFloat(dna[index / 2]);
		return value;
	}

	boolean isSynapse(int possibleSynapseType)
	{
		return findSynapseMap(possibleSynapseType) != null;
	}

	boolean isNeuralLayer(int possibleNeuralLayerType)
	{
		return findNeuralLayerMap(possibleNeuralLayerType) != null;
	}

	Layer layerFactory(int layerType, short rows) throws InstantiationException, IllegalAccessException
	{
		Layer layer = null;
		NeuralLayerMap<? extends Layer> map = findNeuralLayerMap(layerType);
		if (map != null)
			layer = map.instantiate(rows);
		return layer;
	}

	Synapse synapseFactory(int synapseType) throws InstantiationException, IllegalAccessException
	{
		Synapse synapse = null;

		SynapseMap map = findSynapseMap(synapseType);
		if (map != null)
			synapse = map.instantiate();
		return synapse;
	}

	SynapseMap findSynapseMap(int synapseType)
	{
		SynapseMap map = null;

		synapseType %= MAX_MAP_VALUE;
		for (int i = 0; i < mapSynapses.length; i++)
		{

			if (mapSynapses[i].Id == synapseType)
			{
				map = mapSynapses[i];
				break;
			}
		}
		return map;

	}

	/**
	 * Find the Neural layer Map for the given type.
	 * 
	 * @param neuralLayerType
	 * @return
	 */
	NeuralLayerMap<? extends Layer> findNeuralLayerMap(int neuralLayerType)
	{
		NeuralLayerMap<? extends Layer> map = null;

		neuralLayerType %= MAX_MAP_VALUE;

		for (NeuralLayerMap<? extends Layer>  current : mapNeuralLayers)
		{
			if (current.Id == neuralLayerType)
			{
				map = current;
				break;
			}
		}
		return map;

	}

	class NeuralLayerMap<Clazz>
	{
		int Id;

		Class<Clazz> clazzLayer;

		NeuralLayerMap(int Id, Class<Clazz> clazzLayer)
		{
			this.Id = Id;
			this.clazzLayer = clazzLayer;
		}

		Layer instantiate(short rows) throws InstantiationException, IllegalAccessException
		{
			Layer layer = (Layer) clazzLayer.newInstance();
			layer.setRows(rows % MAX_ROWS);
			return layer;
		}
	}

	class SynapseMap // <Clazz>
	{
		int Id;

		Class<? extends Synapse> clazzSynapse;

		SynapseMap(int Id, Class<? extends Synapse> clazzLayer)
		{
			this.Id = Id;
			this.clazzSynapse = clazzLayer;
		}

		Synapse instantiate() throws InstantiationException, IllegalAccessException
		{
			Synapse synapse = null;
			if (clazzSynapse.getName().equals(MyInputSynapse.class.getName()))
				synapse = inputSynapse;
			// else if (clazzSynapse.getName().equals(
			// MyOutputSynapse.class.getName()))
			// synapse = outputSynapse;
			else
				synapse = clazzSynapse.newInstance();
			return synapse;
		}
	}

	SynapseMap[] mapSynapses =
	{ new SynapseMap(SYNAPSE_DIRECT, DirectSynapse.class), new SynapseMap(SYNAPSE_FULL, FullSynapse.class),
			new SynapseMap(SYNAPSE_DELAY, DelaySynapse.class), new SynapseMap(SYNAPSE_KOHONEN, KohonenSynapse.class),
			new SynapseMap(SYNAPSE_SANGER, SangerSynapse.class), new SynapseMap(SYNAPSE_INPUT, MyInputSynapse.class),
			new SynapseMap(SYNAPSE_OUTPUT, MyOutputSynapse.class) };

}