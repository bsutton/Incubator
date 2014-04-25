/*
 * Created on 11/05/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sf.incubator.organisms.brain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import net.sf.incubator.DNAStrand;

import org.joone.engine.InputPatternListener;
import org.joone.engine.Layer;
import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;
import org.joone.engine.OutputPatternListener;
import org.joone.engine.Synapse;
import org.joone.net.NeuralNet;

/**
 * @author bsutton
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Brain implements NeuralNetListener, Cloneable
{
	static int			seedId		= 0;

	int					brainId		= seedId++;

	// DNAStrand strand;
	NeuralNet			nn			= new NeuralNet();

	ArrayList<Layer>	aLayers		= new ArrayList<Layer>(1000);

	private boolean		initialised	= false;

	private MyInputSynapse	inputSynapse;

	private MyOutputSynapse	outputSynapse;


	public String toString()
	{
		return "Brain id=" + brainId;
	}
	// ArrayList aSynapse = new ArrayList();

	Brain(DNAStrand strand)
	{
		// this.strand = strand;
		nn.getMonitor().addNeuralNetListener(this);
	}

	public Brain clone()
	{
		Brain copy = null;
		try
		{
			copy = (Brain) super.clone();
			copy.nn = nn.cloneNet();
		}
		catch (CloneNotSupportedException e)
		{
			// should never happen
			throw new RuntimeException(e);
		}
		return copy;
	}

	public void initialise()
	{
		if (!initialised)
		{
			for (int i = 0; i < aLayers.size(); i++)
			{
				Layer layer = aLayers.get(i);
				@SuppressWarnings("unchecked")
				Vector<InputPatternListener> inputs = layer.getAllInputs();
				@SuppressWarnings("unchecked")
				Vector<OutputPatternListener> outputs = layer.getAllOutputs();
				if (inputs != null && inputs.size() != 0 && outputs != null
						&& outputs.size() != 0)
					nn.addLayer(layer);
			}
			// We don't require the layers anymore.
			aLayers = null;
			initialised = true;
		}
	}

	public void run()
	{
		initialise();
		System.out.println("Layers=" + nn.getLayers().size());
		Monitor monitor = nn.getMonitor();
		
		// TODO each of the following values should be taken from the DNA
		monitor.setLearningRate(0.7);
	    monitor.setMomentum(0.5);
	    monitor.setTrainingPatterns(4); /* # of rows contained in the input file */
	    monitor.setTotCicles(2000); /* How many times the net must be trained on the input patterns */
	    monitor.setLearning(true); /* The net must be trained */
   
		nn.start();
		monitor.Go();
	}

	public void serialize() throws IOException
	{
		String fileName = "brain" + brainId + ".joone";
		String dir = System.getProperty("user.dir");
		File brainFile = new File(dir, fileName);
		System.out.println(brainFile.getCanonicalPath());
		FileOutputStream stream = new FileOutputStream(brainFile);
		ObjectOutputStream out = new ObjectOutputStream(stream);
		out.writeObject(nn);
		out.close();
	}

	/**
	 * @param layer
	 */
	public void addLayer(Layer layer)
	{
		aLayers.add(layer);
		// nn.addLayer(layer);

	}

	/**
	 * @param synapse
	 */
	public void addSynapse(Synapse synapse)
	{
		// aSynapse.add(synapse);

	}

	/**
	 * @param leftValue
	 * @return
	 */
	public Layer getLayer(int leftValue) // UnsignedInteger leftValue)
	{
		Layer layer = null;

		// int index = leftValue.intValue();
		if (leftValue < aLayers.size())
			layer = aLayers.get(leftValue);
		return layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joone.engine.NeuralNetListener#netStarted(org.joone.engine.NeuralNetEvent)
	 */
	public void netStarted(NeuralNetEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joone.engine.NeuralNetListener#cicleTerminated(org.joone.engine.NeuralNetEvent)
	 */
	public void cicleTerminated(NeuralNetEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joone.engine.NeuralNetListener#netStopped(org.joone.engine.NeuralNetEvent)
	 */
	public void netStopped(NeuralNetEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joone.engine.NeuralNetListener#errorChanged(org.joone.engine.NeuralNetEvent)
	 */
	public void errorChanged(NeuralNetEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joone.engine.NeuralNetListener#netStoppedError(org.joone.engine.NeuralNetEvent,
	 *      java.lang.String)
	 */
	public void netStoppedError(NeuralNetEvent arg0, String arg1)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	public int getLayerCount()
	{
		return aLayers.size();
	}

	/**
	 * @return
	 */
	public int getConnectedLayerCount()
	{
		return nn.getLayers().size();
	}

	public void setInputSynapse(MyInputSynapse inputSynapse)
	{
		this.inputSynapse = inputSynapse;
		
	}

	public void setOutputSynapse(MyOutputSynapse outputSynapse)
	{
		this.outputSynapse = outputSynapse;
	}

	public MyInputSynapse getInputSynapse()
	{
		return inputSynapse;
	}

	public MyOutputSynapse getOutputSynapse()
	{
		return outputSynapse;
	}

	public void setLearningRate(float learningRate)
	{
		nn.getMonitor().setLearningRate(learningRate);
		
	}

	public void setMomentum(float momentum)
	{
		nn.getMonitor().setMomentum(momentum);
		
	}

	public void setCycles(short cycles)
	{
		nn.getMonitor().setTotCicles(cycles);
	}
}
