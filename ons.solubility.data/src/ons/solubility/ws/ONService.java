/* Copyright (C) 2008  Pierre Lindenbaum <plindenbaum@yahoo.fr> && the Biogang
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package ons.solubility.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import com.google.gdata.util.AuthenticationException;

import ons.solubility.data.Measurement;
import ons.solubility.data.SolubilityData;



@WebService(
		name="onsolubility",
		serviceName="ons",
		targetNamespace=Measurement.NS
		)
public class ONService
	{
	public static final String Authentication_FILE=".google-authentication";
	
	public ONService()
		{
		
		}
	
	/** retrieve the latest results */
	private static Collection<Measurement> getMeasures() throws Exception
		{
		String login=null;
		String password=null;
		File f= new File(System.getProperty("user.home"),Authentication_FILE); 
		if(!f.exists()) throw new FileNotFoundException(f.toString());
		Properties prop= new Properties();
		Reader r= new FileReader(f);
		prop.load(r);
		r.close();
		login=prop.getProperty("login");
		password=prop.getProperty("password");
		if(login==null) throw new AuthenticationException("unknown login");
		if(password==null) throw new AuthenticationException("unknown password");
		SolubilityData data= new SolubilityData(login,password);
		data.download();
		return data.getData();
		}
	
	private static Double safeToDouble(String s)
		{
		if(s==null) return null;
		try {
			return new Double(s.trim());
			} 
		catch (NumberFormatException e)
			{
			return null;
			}
		}
	
	@WebMethod(action="urn:search",operationName="search")
	public List<Measurement> search(
			@WebParam(name="solute",targetNamespace=Measurement.NS)String solute,
			@WebParam(name="solvent",targetNamespace=Measurement.NS)String solvent,
			@WebParam(name="concMin",targetNamespace=Measurement.NS)Double concMin,
			@WebParam(name="concMax",targetNamespace=Measurement.NS)Double concMax
			) throws Exception
		{
		List<Measurement> items= new ArrayList<Measurement>();
		for(Measurement data: getMeasures())
			{
			if(solute!=null && !solute.equalsIgnoreCase(data.getSolute())) continue;
			if(solvent!=null && !solvent.equalsIgnoreCase(data.getSolvent())) continue;
			Double conc= safeToDouble(data.getConcentration());
			if(conc!=null &&
				(
				  ((concMin!=null && conc< concMin) ||
				   (concMax!=null && conc> concMax))
				)) continue;
			items.add(data);
			}
		return items;
		}
	
	public static void main(String[] args)
		{
		String host="http://localhost";
		int port=8080;
		try
			{
			int optind=0;
			while(optind< args.length)
				{
				if(args[optind].equals("-h"))
					{
					System.err.println("2008. The biogang");
					System.err.println(" -h help");
					System.err.println(" -H <host>. default:"+host);
					System.err.println(" -p <port>. default:"+port);
					return;
					}
				else if(args[optind].equals("-H"))
					{
					host= args[++optind];
					}
				else if(args[optind].equals("-p"))
					{
					port= Integer.parseInt(args[++optind]);
					}
				else if(args[optind].equals("--"))
					{
					optind++;
					break;
					}
				else if(args[optind].startsWith("-"))
					{
					System.err.println("Unknown option "+args[optind]);
					}
				else 
					{
					break;
					}
				++optind;
				}
			
			if(optind!=args.length)
				{
				System.err.println("Illegal Number of arguments");
				return;
				}
			ONService service=new ONService();
			Endpoint endpoint = Endpoint.create(service);
			String url=host+":"+port+"/onsolubility";
			System.out.println("Publishing Service on "+url+"?WSDL");
			endpoint.publish(url);
			} 
		catch(Throwable err)
			{
			err.printStackTrace();
			}
		}
}
