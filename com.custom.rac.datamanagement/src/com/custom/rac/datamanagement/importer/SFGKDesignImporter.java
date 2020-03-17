package com.custom.rac.datamanagement.importer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.custom.rac.datamanagement.util.AbstractImporter;
import com.custom.rac.datamanagement.util.MyClassifyManager;
import com.custom.rac.datamanagement.util.MyDatasetUtil;
import com.custom.rac.datamanagement.util.PropertyContainer;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

/**
 * ͼֽ���빤��
 * @author Administrator
 *
 */
public class SFGKDesignImporter extends AbstractImporter {

	String shared_directory_path = "\\\\192.168.25.11";
	TCSession session = (TCSession) AIFUtility.getDefaultSession();
	MyClassifyManager cls_manger = new MyClassifyManager(session);
	TCComponentFolder folder = null;
	
	@Override
	public String getName() {
		return "ͼֽ�������";
	}

	@Override
	public void onSetPropertyFinish(int index, String propertyDisplayName) throws Exception {

	}

	@Override
	public void onSetPropertyError(int index, String propertyDisplayName, Exception e) throws Exception {
		System.out.println("��" +index+ "���쳣��" + e.getMessage());
	}

	@Override
	public TCComponentItemType getItemType(int index) throws TCException {
		TCSession session = (TCSession) AIFUtility.getDefaultSession();
		String type = getValue(index, "ͼֽ����") + "";
		TCComponentItemType itemType = null;
		switch (type) {
		case "���񽨲�Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_ConstrDesign");
			break;
		case "��ҵ��Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_IndustDesign");
			break;
		case "�����Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_TrackDesign");
			break;
		case "�˵��Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_NucPowDesign");
			break;
		case "������Ʒͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_EnvDesign");
			break;
		case "���ͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_EleDesign");
			break;
		case "���ͼ":
			itemType = (TCComponentItemType) session.getTypeComponent("SF8_PartDesign");
			break;			
		default:
			break;
		}
		return itemType;
	}

	@Override
	public PropertyContainer getPropertyContainer(int index) throws Exception {
		return PropertyContainer.itemRevision;
	}

	@Override
	public void onSingleStart(int index) throws Exception {

	}

	@Override
	public void onSingleFinish(int index, TCComponent tcc) throws Exception {
		if (folder != null) {
			folder.add("contents", tcc);
		}
	}

	@Override
	public void onSingleError(int index, Exception e) throws Exception {
		System.out.println("��" +index+ "���쳣��" + e.getMessage());

	}

	@Override
	public void onStart() throws Exception {
		TCComponentFolderType folderType = (TCComponentFolderType) session.getTypeComponent("Folder");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String name = "ͼֽ��ʷ���ݵ���" + format.format(date);
		folder = folderType.create(name, "", "Folder");
		session.getUser().getHomeFolder().add("contents", folder);
	}

	@Override
	public void onFinish() throws Exception {
		MessageBox.post("�������","��ʾ", MessageBox.INFORMATION);

	}

	@Override
	public boolean ignoreProperty(int index, String propertyDisplayName) throws Exception {
		if (propertyDisplayName.equals("ͼֽ����") ||propertyDisplayName.equals("��װͼ����")
			|| propertyDisplayName.equals("�汾") || propertyDisplayName.equals("ͼֽ����")){
			return true;
		}
		return false;
	}
	
	@Override
	public void setValue(TCComponent tcc, int index, String propertyDisplayName) throws Exception {
		String value = getValue(index, propertyDisplayName)+ "";
		if (propertyDisplayName.equals("ͼ�ĵ�����ID")) {
			cls_manger.saveItemInNode(tcc, value);			
		} else if (propertyDisplayName.equals("���ӵ���ŵ�ַ")) {
			if (value != null && value.length() > 0) {
				if (!value.startsWith("\\") && !value.startsWith("/")) {
					value = "\\" + value;
				}
				value = shared_directory_path + value;
				File file = new File(value);
				if (file != null && file.exists() &&file.isFile()) {
					MyDatasetUtil.createDateset(tcc, file.getName(), file, "IMAN_specification");
				} else {
					throw new Exception("�Ҳ������ݼ��ļ�" + value);
				}
			} else {
				throw new Exception("���ӵ���ŵ�ַ����Ϊ��");
			}			
		} else {
			super.setValue(tcc, index, propertyDisplayName);
		}
	}

	@Override
	public boolean ignoreRow(int index) throws Exception {
		return false;
	}

	@Override
	public boolean deleteOldItemWhenItemIdExist(int index) throws Exception {
		return true;
	}

	@Override
	public void onPropertyRealNameNotFound(int index, String propertyName) throws Exception {
		System.out.println("�ڣ�" + index + "�еģ�" + propertyName + "�����Բ����ڣ�");

	}


}
