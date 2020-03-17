package com.custom.rac.datamanagement.importer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyStatusUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;
import com.teamcenter.rac.kernel.TCSession;

public class SFGKPartImporter extends AbstractImporter {
	
	private static HashMap<String, String> typeMap = new HashMap<String, String>();
	private TCSession session = (TCSession) AIFUtility.getDefaultSession();
	private MyClassifyManager cls_manger = new MyClassifyManager(session);
	private TCComponentItemType itemType = null;
	private TCComponentFolder folder = null;
	protected int folderChildIndex = 0;
	
	static {
		typeMap.put("��Ʒ", "SF8_PPart");
		typeMap.put("���Ʒ", "SF8_SPart");
		typeMap.put("ë��", "SF8_Wpart");
		typeMap.put("ԭ����", "SF8_RPart");
		typeMap.put("���", "SF8_FPart");	
	}
	
	@Override
	public String getName() {
		return "�Ϸ�߿����ϵ������";
	}

	@Override
	public void execute() throws Exception {
		super.execute();
	}
	

	@Override
	public TCComponentItemType getItemType(int index) {
		String type = (String) getValue(index, "��������");
		String reltype = typeMap.get(type);	
		try {		
			itemType = (TCComponentItemType) session.getTypeComponent(reltype);
		} catch (TCException e) {
			e.printStackTrace();
		}	
		return itemType;
	}
	
	@Override
	public void setValue(TCComponent tcComponent, int index, String propertyDisplayName) throws Exception {
		String value = getValue(index, propertyDisplayName)+ "";
		if (propertyDisplayName.equals("����״̬")) {
			MyStatusUtil.setStatus(tcComponent, value);
		}else if(propertyDisplayName.equals("���Ϸ���ID")){
			cls_manger.saveItemInNode(tcComponent, value);	
		}else {
			super.setValue(tcComponent, index, propertyDisplayName);
		}		
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) {
		System.out.println("�� " + index + " �п�ʼ���룡");
	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception{
		putInFolder(tcc);
		System.out.println("�� " + index + " �е�����ϣ�");
	}
	

	@Override
	public void onSingleError(int index, Exception e) {
		System.out.println("�� " + index +"�����ˣ�" + e.toString());
	}

	@Override
	public void onStart() throws Exception{
		System.out.println("���뿪ʼ");
		if(checkProperties()) {
			System.out.println("��Ҫ���Լ��ͨ��");			
		}else {
			throw new Exception("��Ҫ���Լ�鲻ͨ��");
		}
	}

	@Override
	public void onFinish() {
		System.out.println("�����������鿴������־");
	}
	
	static ArrayList<String> ignoreList = new ArrayList<String>();
	static{
		ignoreList.add("���");
		ignoreList.add("���Ϻ�");
		ignoreList.add("����");
	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) {
		return ignoreList.contains(propertyDisplayName);
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) {
		System.out.println("�ڣ�" + index + "�еģ�" + propertyName + "�����Բ����ڣ�");
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) {
		
	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) {
		
	}

	@Override
	public boolean ignoreRow(int index) {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) {
		return false;
	}
	

	/**������Ϻż�������λ
	 * @return
	 * @throws Exception
	 */
	private boolean checkProperties() throws Exception {
		List<Map<String, String>> list = getValues();
		if(list.size()>0) {
			TCComponentType type = getPropertyContainerType(0);
			Map<String, String> nesPriopertiesMap = list.get(0);
			TCPropertyDescriptor tcpdid = type.getPropertyDescriptor("item_id");
			TCPropertyDescriptor tcpduom = type.getPropertyDescriptor("sf8_unit_of_measure");			
			boolean hasItemId = nesPriopertiesMap.containsKey(tcpdid.getDisplayName());
			boolean hasItemUom = nesPriopertiesMap.containsKey(tcpduom.getDisplayName());
			if(hasItemId&&hasItemUom) {
				return true;
			}
		}
		return false;	   
	}
	
	
	
	/**���½�����Ž��ļ���
	 * @param tcitem
	 * @return
	 */
	private String putInFolder(TCComponent tcitem) {
		
		if(folderChildIndex > 500 || folder == null){
			folder = null;
			try {
				initFolder();
			} catch (Exception e) {
				return e.toString();
			}
			
		}else{
			try {
				folder.add("contents", tcitem);
				folderChildIndex++;
			} catch (TCException e) {
				return e.toString();
			}			
		}	
		return "";
	}
	
	/**�����ļ���
	 * @throws Exception
	 */
	private void initFolder() throws Exception {
		
		TCSession session = (TCSession) AIFUtility.getCurrentApplication().getSession();
		TCComponentFolderType ft = (TCComponentFolderType) session.getTypeComponent("Folder");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time=format.format(new Date());
		String folderName = "������ʷ����-"+time;
		folder = ft.create(folderName, "���ڴ�ŵ������ʷ����", "Folder");
		folderChildIndex = 0;
		session.getUser().getHomeFolder().add("contents", folder);
		
	}

}
