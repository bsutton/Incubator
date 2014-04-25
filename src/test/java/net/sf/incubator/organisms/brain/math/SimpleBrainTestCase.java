package net.sf.incubator.organisms.brain.math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.NumberFormat;
import java.util.Iterator;

import junit.framework.TestCase;
import net.sf.incubator.organisms.brain.MyInputSynapse;
import net.sf.incubator.organisms.brain.TestCases;

import org.joone.engine.FullSynapse;
import org.joone.engine.Layer;
import org.joone.engine.LinearLayer;
import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;
import org.joone.engine.Synapse;
import org.joone.engine.TanhLayer;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.FileOutputSynapse;
import org.joone.net.NeuralNet;
import org.joone.util.NormalizerPlugIn;

public class SimpleBrainTestCase extends TestCase implements NeuralNetListener
{
	private MyInputSynapse	inputStream;

	synchronized public void test()
	{
		
		NeuralNet nn = createNet();
		System.out.println("Training network");
		train(nn);
		System.out.println("Testing network");
		interrogate(nn, "src/test/resources/result.csv");
		try
		{
			displayResults("src/test/resources/result.csv");
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayResults(String results) throws NumberFormatException, IOException
	{
		try
		{
//			FileInputStream fis = new FileInputStream(new File(results));
			LineNumberReader lnr = new LineNumberReader(new FileReader(new File(results)));
			NumberFormat nf = NumberFormat.getInstance();
			TestCases cases = MathTestCases.simpleTestCases;
			for (Iterator<net.sf.incubator.organisms.brain.TestCase> iter = cases.iterator(); iter.hasNext();)
			{
				MathTestCase element = (MathTestCase)iter.next();
				System.out.println(element.printExpression() + "[result="
						+ nf.format(new Float(lnr.readLine())) + "]");
				
				
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		
	}

	NeuralNet createNet()
	{
		LinearLayer input = new LinearLayer();
		TanhLayer hidden = new TanhLayer();
		TanhLayer output = new TanhLayer();

		input.setRows(3);
		hidden.setRows(100);
		output.setRows(1);

		FullSynapse synapse_IH = new FullSynapse(); /* Input -> Hidden conn. */
		FullSynapse synapse_HO = new FullSynapse(); /* Hidden -> Output conn. */

		connect(input, synapse_IH, hidden);
		connect(hidden, synapse_HO, output);

		System.out.println("Initialising standard test cases");

		inputStream = new MyInputSynapse(MathTestCases.simpleTestCases);
		inputStream.setAdvancedColumnSelector("1,2,3");
		input.addInputSynapse(inputStream);

		// Normalize the input to the range of -1 to +1 as expected by the
		// TanhLayer
		 NormalizerPlugIn normalizer = new NormalizerPlugIn();
		 normalizer.setAdvancedSerieSelector("1,2,3");
		 normalizer.setDataMax(10.0);
		 normalizer.setDataMin(-10.0);
		 normalizer.setMax(1.0);
		 normalizer.setMin(-1.0);
		 inputStream.addPlugIn(normalizer);

		TeachingSynapse trainer = new TeachingSynapse();

		/* The expected results are in the 4th column of the InputSynapse */
		MyInputSynapse expectedResults = new MyInputSynapse(
				MathTestCases.simpleTestCases);
		expectedResults.setAdvancedColumnSelector("4");

		// Normalize the results to the range of -1 to +1 as output by the
		// TanhLayer
		 NormalizerPlugIn normalizerOutput = new NormalizerPlugIn();
		 normalizerOutput.setAdvancedSerieSelector("1");
		 normalizerOutput.setDataMax(10.0);
		 normalizerOutput.setDataMin(-10.0);
		 normalizerOutput.setMax(1.0);
		 normalizerOutput.setMin(-1.0);
		 expectedResults.addPlugIn(normalizer);

		trainer.setDesired(expectedResults);
		output.addOutputSynapse(trainer);

		NeuralNet nnet = new NeuralNet();
		nnet.addLayer(input, NeuralNet.INPUT_LAYER);
		nnet.addLayer(hidden, NeuralNet.HIDDEN_LAYER);
		nnet.addLayer(output, NeuralNet.OUTPUT_LAYER);

		return nnet;
	}

	void train(NeuralNet nn)
	{
		Monitor monitor = nn.getMonitor();
		monitor.addNeuralNetListener(this);

		monitor.setLearningRate(0.25);
		monitor.setMomentum(0.15);
		monitor.setTotCicles(10); // was 10,000 which is more realistic 
		/*
										 * How many times the net must be
										 * trained on the input patterns
										 */
		monitor.setLearning(true); /* The net must be trained */
		monitor.setSupervised(false);

		// input.setMonitor(monitor);
		// hidden.setMonitor(monitor);
		// output.setMonitor(monitor);
		// trainer.setMonitor(monitor);
		// expectedResults.setMonitor(monitor);
		// inputStream.setMonitor(monitor);

		monitor.setTrainingPatterns(inputStream.size()); /*
															 * # of rows
															 * contained in the
															 * input file
															 */

		System.out.println("Running Brain");

		// input.start();
		// hidden.start();
		// output.start();

		System.out.println(nn.check());

		nn.start();
		monitor.Go(); /* The net starts the training job */
		nn.join();

		double error = monitor.getGlobalError();
		System.out.println("GobalError =" + error);
	}

	private void interrogate(NeuralNet nnet, String outputFile)
	{
		Monitor mon = nnet.getMonitor();
		//input.removeAllInputs();
		//int startRow = trainingPatterns - temporalWindow;
		//input.addInputSynapse(createDataSet(fileName, startRow + 1,
		//		startRow + 20, "1"));
		//output.removeAllOutputs();
		FileOutputSynapse fOutput = new FileOutputSynapse();
		fOutput.setFileName(outputFile);
		Layer output = nnet.getOutputLayer();
		output.addOutputSynapse(fOutput);
		mon.setTrainingPatterns(inputStream.size());
		mon.setTotCicles(1);
		mon.setLearning(false);
		nnet.start();
		mon.Go();
		nnet.join();
	}

	private void connect(Layer layer1, Synapse syn, Layer layer2)
	{
		layer1.addOutputSynapse(syn);
		layer2.addInputSynapse(syn);
	}

	public void netStarted(NeuralNetEvent arg0)
	{
		System.out.println("Net Started ");

	}

	public void cicleTerminated(NeuralNetEvent e)
	{
		org.joone.engine.Monitor mon = (org.joone.engine.Monitor) e.getSource();
		long c = mon.getCurrentCicle();
		long cl = c / 1000;
		// print the results every 1000 cycles
		if ((cl * 1000) == c)
		{
			System.out.println(c + " cycles remaining - Error = "
					+ mon.getGlobalError());
		}
	}

	synchronized public void netStopped(NeuralNetEvent arg0)
	{
		// notifyAll();

	}

	public void errorChanged(NeuralNetEvent arg0)
	{
		// System.out.println("errorChanged " + arg0);

	}

	synchronized public void netStoppedError(NeuralNetEvent arg0, String arg1)
	{
		System.out.println("netStoppedError ");
		notifyAll();

	}

}
