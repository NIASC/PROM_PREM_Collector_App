package Testing;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import core.PROM_PREM_Collector;

public class Main
{
	public static void main(String[] args)
	{
		PROM_PREM_Collector ppc = new PROM_PREM_Collector();
		ppc.start();
	}
}
