package com.thekratos.test.IpCleaner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.thekratos.IpCleaner.IpRange;
import com.thekratos.IpCleaner.IpReader;
import com.thekratos.IpCleaner.ProcessIpRanges;


public class TestIpCleaner {

	@Test
	public void testDistinctIdealCase(){
		IpRange range1 = new IpRange(100, 150, "Airtel", 1);
		IpRange range2 = new IpRange(151, 160, "Airtel", 2);
		IpRange range3 = new IpRange(180, 200, "Aircel", 3);
		IpRange range4 = new IpRange(201, 250, "Docomo", 4);

		IpRange range5 = new IpRange(10, 50, "Aircel", 5);
		IpRange range6 = new IpRange(60, 70, "Docomo", 6);
		
		List<IpRange> rangeList = new ArrayList<>();
		rangeList.add(range1);
		rangeList.add(range2);
		rangeList.add(range3);
		rangeList.add(range4);

		rangeList.add(range5);
		rangeList.add(range6);
		
		ProcessIpRanges processor = new ProcessIpRanges(rangeList);
		processor.process();
		
		processor.printTree();
		
	}
	
	@Test
	public void testOverlappedIp(){
		IpRange range1 = new IpRange(100, 150, "Airtel", 1);
		IpRange range2 = new IpRange(140, 160, "Airtel", 2);
		IpRange range3 = new IpRange(120, 140, "Airtel", 3);
		IpRange range4 = new IpRange(201, 250, "Docomo", 4);
		
		List<IpRange> rangeList = new ArrayList<>();
		rangeList.add(range1);
		rangeList.add(range2);
		rangeList.add(range3);
		rangeList.add(range4);
		
		ProcessIpRanges processor = new ProcessIpRanges(rangeList);
		processor.process();
		
		processor.printTree();
		
	}
	
	
	@Test
	public void testOverlappedIpConflictingCarrier(){
		IpRange range1 = new IpRange(100, 150, "Airtel", 1);
		IpRange range2 = new IpRange(140, 160, "Aircel", 2);
		IpRange range3 = new IpRange(120, 140, "Airtel", 3);
		IpRange range4 = new IpRange(201, 250, "Docomo", 4);
		
		List<IpRange> rangeList = new ArrayList<>();
		rangeList.add(range1);
		rangeList.add(range2);
		rangeList.add(range3);
		rangeList.add(range4);
		
		ProcessIpRanges processor = new ProcessIpRanges(rangeList);
		processor.process();
		
		processor.printTree();
		
	}
	
	@Test
	public void testOverlappedIpConflictingCarrierleftSubTree(){
		
		IpRange range2 = new IpRange(140, 160, "Airtel", 2);
		IpRange range3 = new IpRange(120, 140, "Airtel", 3);
		IpRange range1 = new IpRange(100, 150, "Aircel", 1);
		IpRange range4 = new IpRange(201, 250, "Docomo", 4);
		IpRange range5 = new IpRange(220, 260, "Docomo", 5);
		IpRange range6 = new IpRange(10, 150, "Airtel", 6);
		IpRange range7 = new IpRange(90, 110, "Idea", 7);
		
		List<IpRange> rangeList = new ArrayList<>();
		rangeList.add(range2);
		rangeList.add(range3);
		rangeList.add(range1);
		rangeList.add(range4);

		rangeList.add(range5);
		rangeList.add(range6);
		rangeList.add(range7);
		
		ProcessIpRanges processor = new ProcessIpRanges(rangeList);
		processor.process();
		
		processor.printTree();
		
	}
	
	@Test
	public void testActualCsv() throws Exception{
		IpReader reader = new IpReader();
		ProcessIpRanges processor = new ProcessIpRanges(reader.getRanges());
		processor.process();
		processor.printTree();
		processor.verifyTree();
		processor.outputToCleanedCsv();
		
	}

}
