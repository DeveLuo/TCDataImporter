package com.custom.rac.datamanagement.importer;

import java.util.HashMap;
import java.util.Map;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.BOMUtil;
import com.custom.rac.datamanagement.util.MyBOMUtil;
import com.custom.rac.datamanagement.util.MyItemUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevision;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevisionType;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentViewType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKBomImporter extends AbstractImporter {
	
	MyItemUtil myItemUtil = null;
	public final String BOM_VIEW_TYPE = "view";
	private TCComponentViewType viewType = null;
	private TCComponentBOMViewRevisionType viewRevType = null;
	private Boolean ForceUpdateFlag = true;
	
	@Override
	public String getName() {
		
		return "�Ϸ�߿�BOM�������";
		
	}
	
	@Override
	public void onSingleMessage(int index, String msg) throws Exception {
		System.out.println("��" +index+ "��:"+msg);		
	}	
	

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) throws Exception {

		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public TCComponentItemType getItemType(int index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSingleStart(int index) throws Exception {
		System.out.println("��" +index+ "�п�ʼ");		
	}

	@Override
	public void onSingleFinish(int index, TCComponent newInstance) throws Exception {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleError(int index, Exception e) throws Exception {
		System.out.println("��" +index+ "���쳣��" + e.getMessage());
	}

	@Override
	public void onStart() throws Exception {
		System.out.println("BOM���뿪ʼ");		
		
	}

	@Override
	public void onFinish() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ignoreRow(int index) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void execute() throws Exception {
		
		boolean hasError = false;
		TCComponentItemRevision parentObj = null;
		TCComponentItemRevision lineObj = null;
		HashMap<Integer, TCComponent> children = new HashMap<Integer, TCComponent>();
		String parentId = null;
		String parentId2 = null;
		String lineId = null;
		onStart();
		driver.onStart();
		myItemUtil = new MyItemUtil("Item");
		viewType = MyBOMUtil.getViewType(BOM_VIEW_TYPE);
		if (viewType == null) {
			return;
		}
		viewRevType = MyBOMUtil.getTCComponentBOMViewRevisionType();
		if (viewRevType == null) {
			return;
		}
		int i = 0;
		while(i<=values.size()-1){
			
			driver.onSingleMessage(i, "���뿪ʼ...");			
			int lastRow = i;
			int lineCount = 0;
			parentId = getValue(i, "����ID")+ "";
			lineId = getValue(i, "����ID")+"";
			if (lineId.length()>0) {
				lineCount++;
			}
			for (int j=i+1; j<values.size(); j++) {
				parentId2 = getValue(j,"����ID" )+ "";
				if (parentId.equals(parentId2)) {
					lineCount++;
					lastRow = j;
				}
				else {
					break;
				}
			}
			if (lineCount == 0) {
				driver.onSingleError(i, new Exception("û�нṹBOM�����Դ���"));

			}
			parentObj = queryObj(parentId);
			if (parentObj == null) {
				hasError = true;				
				driver.onSingleError(i, new Exception("�������ϲ����ڣ��޷����뵱ǰBOM��"));
				
			}
			boolean linesOK = true;
			children.clear();

			for (int j=i; j<=lastRow; j++) {
				lineId = getValue(j, "����ID")+"";
				if (lineId.length() == 0) {
					continue;
				}				
				lineObj = queryObj(lineId);
				if (lineObj == null) {
					linesOK = false;
					hasError = true;
					driver.onSingleError(j, new Exception("��������󲻴��ڣ�����BOM�޷�����"));
					continue;
				}				
				if (parentObj!=null&&lineObj!=null) {
					driver.onSingleMessage(j, ">>��BOM�м��ok");
					children.put(j, lineObj);
				}
			}
			if(!linesOK) {
				for (int z = i; z <=lastRow ; z++) {
					driver.onSingleError(z, new Exception(">>����ʧ�ܣ������������ݼ��ʧ�ܣ��޷����뵱ǰBOM��"));
				}
			}
			if(parentObj==null) {
				for (int z = i; z <=lastRow ; z++) {
					driver.onSingleError(z, new Exception(">>����ʧ�ܣ��������ϲ����ڣ��޷����뵱ǰBOM��"));
				}
			}
			i = lastRow;
			i++;
			if(parentObj==null||!linesOK) {
				continue;
			}

			TCComponentBOMViewRevision oldBvr = null;
			int removeFlag = 0;
			try {
				oldBvr = BOMUtil.findBVR(parentObj, viewType);
				if (oldBvr != null) {
					if (ForceUpdateFlag) {
						driver.onSingleMessage(i-1, ">>�ṹBOM�Ѿ����ڣ�����ɾ���ؽ���");
						removeFlag = -1;
					}
					else {
						driver.onSingleMessage(i-1, ">>�ṹBOM�Ѿ����ڣ������ظ������BOM��");
						continue;
					}
				}
			}
			catch(Exception ex) {
				hasError = true;
				driver.onSingleMessage(i-1, "����BOMʧ��"+ex.toString());
				continue;
			}

			String ret = null;
			if (removeFlag < 0) {
				ret = createStructureBOM(parentObj, children, oldBvr);
			}else {
				ret = createStructureBOM(parentObj, children, null);
			}
			
			//���뱣��
			if (ret == null) {
				driver.onSingleMessage(i-1, ">>�ṹBOM����ɹ���");
			}else {
				hasError = true;
				driver.onSingleError(i+1, new Exception(parentId + ">>�ṹBOM����ʧ�ܣ�"));
			}		
		}		
		onFinish();
	}
	
	private TCComponentItemRevision queryObj(String itemId) {
		TCComponentItemRevision ret = null;
		try {
			TCComponentItem item = myItemUtil.find(itemId);
			if (item != null) {
				ret = item.getLatestItemRevision();
			}
		}
		catch(Exception e) { 
			
		}		
		return ret;
	}
	
	private String createStructureBOM(TCComponentItemRevision parentRev, HashMap<Integer, TCComponent> children, TCComponentBOMViewRevision bvr0) {
		String ret = null;
		
		TCComponentBOMWindow bomWin = null;
		TCComponentBOMViewRevision bvr = null;
		try {
			String itemId = parentRev.getItem().getProperty("item_id");
			String revId = parentRev.getProperty("current_revision_id");
			
			if (bvr0 == null) {
				//itemId, revId, viewType, isPrecise
				bvr = viewRevType.create(itemId, revId, viewType, true);
			}
			else {
				bvr = bvr0;
			}
			
			TCSession ss = (TCSession) AIFUtility.getDefaultSession();
			TCComponentBOMWindowType type = (TCComponentBOMWindowType)ss.getTypeComponent("BOMWindow");
			bomWin  = type.create(null);

			TCComponentBOMLine topLine = bomWin.setWindowTopLine(null, null, null, bvr);

			topLine.setPrecision(false);
			
			if (bvr0 != null) {
//				ui.addMsg("��ʼɾ��BOM�ṹ��");
//				topLine.setPrecision(true);
				AIFComponentContext[] ctx = topLine.getChildren();
				if (ctx != null) {
					for (AIFComponentContext c : ctx) {
						if (!(c.getComponent() instanceof TCComponentBOMLine)) continue;
						TCComponentBOMLine line = (TCComponentBOMLine)c.getComponent();
						line.cut();
					}
				}
				
				topLine.save();
				bomWin.save();
//				ui.addMsg("ɾ��BOM�ṹ�ɹ�");
			}

			String v = null;
			TCComponentBOMLine subLine = null;
//			ui.addMsg("��ʼ�BOM�ṹ");
			
			for (Integer i : children.keySet()) {
				TCComponent comp = children.get(i);
				subLine = topLine.addBOMLine(topLine, comp, null);
				
//				���ð汾�����ϵ�
				v = getValue(i, "����")+"";
				subLine.setProperty("bl_quantity", v);									
				subLine.save();
			}
			topLine.save();
			bomWin.save();
			bomWin.close();
			
		}
		catch(Exception e) {
			e.printStackTrace();
//			ui.addMsg(parentRev + ":"+e.getMessage());
			ret = "�����ṹBOMʱ����" + e.getMessage();
			BOMUtil.removeBOM(bomWin, bvr);
		}		
		return ret;
	}



}
