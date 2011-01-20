package ons.solubility.ws.test;

import java.util.List;

public class ONServiceTest {
private	ONServiceTest(){}

private void test(
	String solute,
	String solvent,
	Double concMin,
	Double concMax)
	{
	System.out.println("##Searching" +
			" solute: "+solute+
			" solvent: "+solvent+
			" conc: "+concMin+"-"+concMax
			);
	try
	{
	Ons service=new Ons();
	Onsolubility port=service.getOnsolubilityPort();
	List<Measurement> data=port.search(solute, solvent, concMin, concMax);
	for(Measurement measure:data)
		{
		System.out.println(
			"  sample    :\t"+measure.getSample()+"\n"+
			"  solute    :\t"+measure.getSolute()+"\n"+
			"  solvent    :\t"+measure.getSolvent()+"\n"+
			"  experiment:\t"+measure.getExperiment()+"\n"+
			"  reference :\t"+measure.getReference()+"\n"+
			"  conc      :\t"+measure.getConcentration()+"\n"
			);
		}
	} catch(Throwable err)
	
	{
	System.err.println("#error:"+err.getMessage());	
	}
	}
private void test()
	{
	test(null,null,null,null);
	test("4-nitrobenzaldehyde",null,null,null);
	test("4-nitrobenzaldehyde",null,0.3,0.4);
	}

public static void main(String[] args)
	{
	new ONServiceTest().test();
	}
}
