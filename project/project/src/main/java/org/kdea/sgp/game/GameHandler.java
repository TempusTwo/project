package org.kdea.sgp.game;

import java.io.IOException;
import java.security.Key;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GameHandler extends TextWebSocketHandler {
	private static Map<String, WebSocketSession> sessionMap = new HashMap<String, WebSocketSession>();

	private static Map<Integer, Map> listMemberMap = new HashMap<Integer, Map>();

	private static Map<Integer, Map> listDataMap = new HashMap<Integer, Map>();

	private static Map<Integer, Map> gameMembersMap = new HashMap<Integer, Map>();

	private static Map<Integer, Map> gameDataMap = new HashMap<Integer, Map>();

	private int rnum = 1;
	private int gnum = 1;

	// ������ �������� �ؽ�Ʈ �޽����� �����Ǹ� ȣ��Ǵ� �޼ҵ�
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// Ŭ���̾�Ʈ���� �ö�� �޽����� ���ڿ��� �ٲ۴�
		String payloadMessage = (String) message.getPayload();

		// ����� ������ ���̽� ���ڿ��� ���� �ϰ� �ȴ�(������ �ڵ鷯�� 1���� �ִ°� �������̱⿡ ���� �پ��� ����� �ޱ� ���ؼ���
		// ���̽����ڿ��� �ʼ��� �ȴ�) �׷��⿡ �������� ����� �ް� �Ǹ� ���̽� �ļ��� ȣ���Ѵ�.
		JSONParser jsonParser = new JSONParser();

		try {
			// ���̽� �ļ��� ���� ���ڿ��� ���̽� ������Ʈ�� �����.
			JSONObject jsonObj = (JSONObject) jsonParser.parse(payloadMessage);

			// ������Ʈ ���� ������ �ܰ躰�� ���������μ� ����� �����ϰԵȴ�. �켱������ �󸶳� ���� ������ ���������� ���°���
			// �������� �Ѵ�. �׷� �ǹ̿��� ���� �߿��Ѱ��� ��� ������ ���������� ����� �Դ°��ϴ°� �� ��ġ�� �˾ƺ��� ����
			// �ȴ�.
			// �׷� �ǹ̿��� �Ǹ��� '��ġ' ������ ���� �޴´�(���� ����Ǵ� ����̶�� �ص� �Ⱦ��� �������� ������ ������ ����
			// �굵�� ������ �����. ���������� �ڵ� ���ຸ�� ���� �ʿ����� �켱�ߴ�.)
			String position = (String) jsonObj.get("position");

			try {
				// ������ �������� '�� ���'�ΰ�� �ҷ������� ��ɵ�
				if (position.equals("roomList")) {
					// ��ġ�� '���'�ΰ��� Ȯ���� �ڿ� '���'�� �˻��Ѵ�.
					String cmd = (String) jsonObj.get("cmd");
					// ����� ä���ΰ�� ���� ��� �������� ���ӵǾ� �ִ� ����(���� �ʿ� ��� ���ǵ� ����)���� �޽�����
					// �����Ѵ�
					if (cmd.equals("chat")) {

						Iterator<String> sessionkeys = sessionMap.keySet().iterator();
						while (sessionkeys.hasNext()) {
							String key = sessionkeys.next();
							sessionMap.get(key).sendMessage(new TextMessage(payloadMessage));
						}

					}
					// ����� '�� �����'�ΰ��
					else if (cmd.equals("roomCreate")) {
						// �� �̸��� ���� ���� ����� �̸��� �޾Ƴ���
						String nick = (String) jsonObj.get("nick");
						String roomName = (String) jsonObj.get("roomName");

						// �������� ������ ����� ���� �����.
						Map<String, String> roomDataMap = new HashMap<String, String>();

						// �����ʿ� ���� ������ ���� �ִ´�
						roomDataMap.put("master", nick);
						roomDataMap.put("roomName", roomName);
						roomDataMap.put("memCnt", "0");
						roomDataMap.put("roomCondition", "ready");

						// ������ ������ ���� ���� ���� ���� ����� �ش���� ��� �뵵�� ����Ʈ�� �����Ѵ�.
						// �� ���� ������ Ű�� �̿��ؼ� ���ʿ��� ���� ������ ���� ��´�.(���� ������ ������ ����Ǵ�
						// 2������ ���� �����.)
						listMemberMap.put(rnum, new HashMap<String, WebSocketSession>());
						listDataMap.put(rnum, roomDataMap);

						// ��û�ڿ��� ��ȯ�� ���̽� ������Ʈ�� ����� ���� ����������� �����϶�� ��ɰ� ���� ��ȣ��
						// ��´�.
						JSONObject reTurnJson = new JSONObject();
						reTurnJson.put("cmd", "enter");
						reTurnJson.put("rnum", rnum);

						// ���̽� ������Ʈ�� ���ڿ��� �����.
						String sJson = reTurnJson.toJSONString();

						// �� �ۼ��ڿ��� ����� ����������
						sessionMap.get(nick).sendMessage(new TextMessage(sJson));

						// �� ������ �������� ���Ͽ� Ű���� �ϳ� �÷��д�
						rnum += 1;

					}
					if (cmd.equals("enterCheck")) {
						String nick = (String) jsonObj.get("nick");
						int roomNum = Integer.parseInt((String) jsonObj.get("rnum"));
						Map<String, String> roomDataMap = listDataMap.get(roomNum);
						int memCnt = Integer.parseInt(roomDataMap.get("memCnt"));
						String roomCondition = roomDataMap.get("roomCondition");

						JSONObject reTurnJson = new JSONObject();
						if (memCnt < 2 && roomCondition.equals("ready")) {

							reTurnJson.put("cmd", "enter");
							reTurnJson.put("rnum", roomNum);

							// �� �ۼ��ڿ��� ����� ����������

						} else {

							reTurnJson.put("cmd", "enterFalse");

							// ���̽� ������Ʈ�� ���ڿ��� �����.

						}
						// �� �ۼ��ڿ��� ����� ����������
						String sJson = reTurnJson.toJSONString();
						sessionMap.get(nick).sendMessage(new TextMessage(sJson));
					}
				} // ��ġ�� �濡�� �� ����ΰ�� (���� ä�ñ�ɸ� ����־ ���� ����� �ν����� �ʴ´�. ��� ������ �Ŀ�
					// �ݵ�� ���� ����̱⿡ �簻�� ����)
				else if (position.equals("room")) {

					String cmd = (String) jsonObj.get("cmd");
					if (cmd.equals("chat")) {
						Map<String, Object> map = session.getAttributes();
						int rnum = (Integer) map.get("rnum");
						Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(rnum);

						Iterator<String> keys = roomMemberMap.keySet().iterator();
						while (keys.hasNext()) {
							String key = keys.next();
							roomMemberMap.get(key).sendMessage(new TextMessage(payloadMessage));
						}
					} else if (cmd.equals("invite")) {
						String receiver = (String) jsonObj.get("receiver");
						sessionMap.get(receiver).sendMessage(new TextMessage(payloadMessage));
						;
					} else if (cmd.equals("kickout")) {
						String nick = (String) jsonObj.get("nick");
						Map<String, Object> map = session.getAttributes();
						int rnum = (Integer) map.get("rnum");
						Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(rnum);

						roomMemberMap.get(nick).sendMessage(new TextMessage(payloadMessage));
					} else if (cmd.equals("ready")) {
						Map<String, Object> map = session.getAttributes();
						int rnum = (Integer) map.get("rnum");
						Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(rnum);
						Iterator<String> roomMemberKeys = roomMemberMap.keySet().iterator();
						while (roomMemberKeys.hasNext()) {
							String key = roomMemberKeys.next();
							roomMemberMap.get(key).sendMessage(new TextMessage(payloadMessage));
							System.out.println(key + "���� ���� �Ϸ�");
						}
					} else if (cmd.equals("start")) {
						Map<String, Object> map = session.getAttributes();
						int rnum = (Integer) map.get("rnum");

						Map<String, String> gameData = new HashMap<String, String>();
						gameData.put("gameCondition", "game");
						gameData.put("memCnt", "0");

						gameMembersMap.put(gnum, new HashMap<String, WebSocketSession>());
						gameDataMap.put(gnum, gameData);

						Map<String, String> roomDataMap = listDataMap.get(rnum);
						String roomCondition = "game";
						roomDataMap.put("roomCondition", roomCondition);

						JSONObject startCmd = new JSONObject();
						startCmd.put("cmd", "start");
						startCmd.put("gnum", gnum);
						

						String sJson = startCmd.toJSONString();

						Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(rnum);
						Iterator<String> roomMemberKeys = roomMemberMap.keySet().iterator();
						while (roomMemberKeys.hasNext()) {
							String key = roomMemberKeys.next();
							roomMemberMap.get(key).sendMessage(new TextMessage(sJson));
							System.out.println(key + "���� ���� �Ϸ�");
						}
						gnum += 1;
						roomListupdate();
					}
				} else if (position.equals("game")) {
					String cmd = (String) jsonObj.get("cmd");
					if (cmd.equals("end")) {
						Map<String, Object> map = session.getAttributes();
						int gnum = (Integer) map.get("gnum");
						Map<String, WebSocketSession> gameMember = gameMembersMap.get(gnum);
						Iterator<String> Keys = gameMember.keySet().iterator();
						while (Keys.hasNext()) {
							String key = Keys.next();
							gameMember.get(key).sendMessage(new TextMessage(payloadMessage));
							System.out.println(key + "���� ���� �Ϸ�");
						}
					}
					if (cmd.equals("playing")) {
						Map<String, Object> map = session.getAttributes();
						int gnum = (Integer) map.get("gnum");
						String nick = (String) map.get("nick"); // ���� ������ ������ ��
						
						//System.out.println(payloadMessage);
						
						Map<String, WebSocketSession> gameMember = gameMembersMap.get(gnum);
						Iterator<String> Keys = gameMember.keySet().iterator();
						while (Keys.hasNext()) {
							String key = Keys.next();
							if(!key.equals(nick)){
								gameMember.get(key).sendMessage(new TextMessage(payloadMessage));
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	// ������ ������ Ŭ���̾�Ʈ�� �����ϸ� ȣ��Ǵ� �޼ҵ�
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		super.afterConnectionEstablished(session);

		// ó�� ���� ������ �ʼ������� �޾ƾ��ϴ� ���� ��� ���������� �����ߴ°��� �����ڸ��̹Ƿ� �� ���� �޾Ƴ���(��� �ʿ� ���ǰ�
		// �г����� ���������� �˱� ���� �ʿ��ϴ�)
		Map<String, Object> map = session.getAttributes();
		String nick = (String) map.get("usrNick");
		String position = (String) map.get("position");

		// �������� '�� ���'�� ���
		if (position.equals("roomList")) {
			// ���Ǹʿ� �г����� Ű�� ������ �����Ѵ�
			sessionMap.put(nick, session);

			// �����ڿ��� ������ ������ �����ϱ� ���� ���̽� ������Ʈ�� �迭�� �غ��Ѵ�.
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("cmd", "roomList");
			JSONArray jsonArr = new JSONArray();

			// ������ ����� ���� �������� ����� Ű���� �޾Ƴ���
			Iterator<Integer> keys = listDataMap.keySet().iterator();
			while (keys.hasNext()) {
				int key = keys.next();

				// ����Ʈ�� ���� ���̽� ������Ʈ�� �����. �׸��� ���������� Ű���� ���� ��ȣ�̹Ƿ� �ٷ� ������Ʈ�� ��´�
				JSONObject listData = new JSONObject();
				listData.put("rnum", key);

				// �������� ��Ͽ��� ������ ���� Ű���� �̿��� �������� �� �ȿ��� ���� ������ �̾Ƴ��� ���̽� ������Ʈ�� ��´�
				Map<String, String> roomDataMap = listDataMap.get(key);
				String roomName = roomDataMap.get("roomName");
				listData.put("roomName", roomName);

				// �ϼ��� ���̽� ������Ʈ�� �迭�� ��´�
				jsonArr.add(listData);
			}

			// ��ȯ�� ���̽� ������Ʈ�� �ϼ��� �迭�� ��� ���ڿ��� �����
			jsonObj.put("list", jsonArr);
			String sJson = jsonObj.toJSONString();

			// ���Ͽ� ������ �������� �����ش�.
			sessionMap.get(nick).sendMessage(new TextMessage(sJson));

			memListupdate();

		}

		// �������� '��'�� ���
		else if (position.equals("room")) {

			// �Ķ���ͷ� ���ͼ��Ϳ��� �޾Ƴ� �� ������ �̾Ƴ��� �ش� �� ���ڸ� Ű�� �ϴ� �� �� �ҷ����� �г��Ӱ� ������
			// ��´�(�Ʒ����� ����Ʈ ���� ����̳� �̶��� ���ǿ� ���� ���� ���ΰ� ������ �����̹Ƿ� ���� �����Ѵ�)
			int rnum = (Integer) map.get("rnum");
			Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(rnum);
			roomMemberMap.put(nick, session);
			System.out.println("�� ���� �Ϸ�");
			Map<String, String> roomDataMap = listDataMap.get(rnum);
			int memCnt = Integer.parseInt(roomDataMap.get("memCnt")) + 1;
			roomDataMap.put("memCnt", Integer.toString(memCnt));
			System.out.println("�� �ο��� ����:" + memCnt);
			if (memCnt > 1) {
				String roomCondition = "ready";
				roomDataMap.put("roomCondition", roomCondition);
			}
			roomUpdate(roomMemberMap, roomDataMap);
			// �� ���� �ο����� �� ������ �����Ѵ�.
			JSONObject roomData = new JSONObject();
			roomData.put("cmd", "roomUpdate");

			JSONArray roomMembers = new JSONArray();
			Iterator<String> roomMemNames = roomMemberMap.keySet().iterator();
			while (roomMemNames.hasNext()) {
				String key = roomMemNames.next();
				roomMembers.add(key);
			}
			roomData.put("roomMembers", roomMembers);

			JSONArray memList = new JSONArray();
			Iterator<String> members = sessionMap.keySet().iterator();
			while (members.hasNext()) {
				String member = members.next();
				memList.add(member);
			}
			roomData.put("list", memList);
			roomData.put("master", roomDataMap.get("master"));
			roomData.put("memCnt", roomDataMap.get("memCnt"));

			String sJson = roomData.toJSONString();

			Iterator<String> roomMemberKeys = roomMemberMap.keySet().iterator();
			while (roomMemberKeys.hasNext()) {
				String key = roomMemberKeys.next();
				roomMemberMap.get(key).sendMessage(new TextMessage(sJson));
				System.out.println(key + "���� ���� �Ϸ�");
			}
			System.out.println("�� ������Ʈ �Ϸ�");
			roomListupdate();
		}
		//�������� '����'�ΰ��
		else if (position.equals("game")) {

			// �Ķ���ͷ� ���ͼ��Ϳ��� �޾Ƴ� �� ������ �̾Ƴ��� �ش� �� ���ڸ� Ű�� �ϴ� �� �� �ҷ����� �г��Ӱ� ������
			// ��´�(�Ʒ����� ����Ʈ ���� ����̳� �̶��� ���ǿ� ���� ���� ���ΰ� ������ �����̹Ƿ� ���� �����Ѵ�)
			int gnum = (Integer) map.get("gnum");
			Map<String, WebSocketSession> gameMember = gameMembersMap.get(gnum);
			gameMember.put(nick, session);
			System.out.println("�� ���� �Ϸ�");
			Map<String, String> gameData = gameDataMap.get(gnum);
			int memCnt = Integer.parseInt(gameData.get("memCnt")) + 1;
			gameData.put("memCnt", Integer.toString(memCnt));
			System.out.println("���� �ο��� ����:" + memCnt);
			if (memCnt == 2) {
				JSONObject startCmd = new JSONObject();
				startCmd.put("cmd", "start");

				String sJson = startCmd.toJSONString();

				Iterator<String> Keys = gameMember.keySet().iterator();
				while (Keys.hasNext()) {
					String key = Keys.next();
					gameMember.get(key).sendMessage(new TextMessage(sJson));
				}
				System.out.println("�� ������Ʈ �Ϸ�");
				
			}
			
			roomListupdate();
		}
		
		System.out.println("Ŭ���̾�Ʈ ���ӵ�");

	}

	// Ŭ���̾�Ʈ�� ������ �����ϸ� ȣ��Ǵ� �޼ҵ�
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);

		// ���� ����� ������ �г����� �޴´�
		Map<String, Object> map = session.getAttributes();
		String nick = (String) map.get("usrNick");

		// �г����� ������ ���Ǹ��� Ƣ�� �ش� ������ ������ �ÿ� ������ �����.
		Iterator<String> listkeys = sessionMap.keySet().iterator();
		while (listkeys.hasNext()) {
			String key = listkeys.next();
			if (key.equals(nick)) {
				sessionMap.remove(nick);
				System.out.println(nick + "�� ����Ʈ ��������");
				memListupdate();
			}

		}

		// �������� �濡 �Ҽӵ� ������ ������ ������ �ǳ� �ణ�� �ܰ踦 �ʿ�� �Ѵ�. �ϴ� �� ��� ������ �ϴ� ����Ʈ ��� ����
		// Ű�� �����´�
		Iterator<Integer> roomkeys = listMemberMap.keySet().iterator();
		while (roomkeys.hasNext()) {
			// Ű���� ����ؼ� ����Ʈ ����� ������ �� ���� �г��Ӱ� ������ ��� ���� �ҷ�����.
			int key = roomkeys.next();
			Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(key);

			// �ش���� ���� ��ġ�ϴ� �г����� ���� ��� �ش� ������ �����Ѵ�.(���� ���ǿ� ���� ����ü ������ ���� ����)
			Iterator<String> roomkeys2 = roomMemberMap.keySet().iterator();
			while (roomkeys2.hasNext()) {
				String key2 = roomkeys2.next();
				if (key2.equals(nick)) {
					roomMemberMap.remove(nick);
					System.out.println(nick + "��  ��������");

					// ���� ����� ���� �������� �ο����� ������.
					Map<String, String> roomDataMap = listDataMap.get(key);
					int memCnt = Integer.parseInt(roomDataMap.get("memCnt")) - 1;
					System.out.println("���� �� �ο���" + memCnt);
					roomDataMap.put("memCnt", Integer.toString(memCnt));

					// ���� ���� �ο����� 0�̶�� ���� ���¸� üũ�Ͽ� �Ϲ� ������Ͻ� ���� �����.
					if (memCnt == 0) {
						if (roomDataMap.get("roomCondition").equals("ready")) {
							listDataMap.remove(key);
							listMemberMap.remove(key);

							roomListupdate();

						}
					} else {
						String master = roomDataMap.get("master");
						if (nick.equals(master)) {
							Iterator<String> remainMember = roomMemberMap.keySet().iterator();
							if (remainMember.hasNext()) {
								String nextMaster = remainMember.next();
								roomDataMap.put("master", nextMaster);
							}
						}
						roomUpdate(roomMemberMap, roomDataMap);
					}

				}

			}

		}
		
		
		//
		Iterator<Integer> gamekeys = gameMembersMap.keySet().iterator();
		while (gamekeys.hasNext()) {
			// Ű���� ����ؼ� ����Ʈ ����� ������ �� ���� �г��Ӱ� ������ ��� ���� �ҷ�����.
			int key = gamekeys.next();
			Map<String, WebSocketSession> gameMember = gameMembersMap.get(key);

			// �ش���� ���� ��ġ�ϴ� �г����� ���� ��� �ش� ������ �����Ѵ�.(���� ���ǿ� ���� ����ü ������ ���� ����)
			Iterator<String> gamekeys2 = gameMember.keySet().iterator();
			while (gamekeys2.hasNext()) {
				String key2 = gamekeys2.next();
				if (key2.equals(nick)) {
					gameMember.remove(nick);
					System.out.println(nick + " ����  ��������");

					// ���� ����� ���� �������� �ο����� ������.
					Map<String, String> gameData = gameDataMap.get(key);
					int memCnt = Integer.parseInt(gameData.get("memCnt")) - 1;
					System.out.println("���� ���� ������" + memCnt);
					gameData.put("memCnt", Integer.toString(memCnt));

					// ���� ���� �ο����� 0�̶�� ���� ���¸� üũ�Ͽ� �Ϲ� ������Ͻ� ���� �����.
					if (memCnt == 0) {
							gameDataMap.remove(key);
							gameMembersMap.remove(key);
							System.out.println("���� ����");
					} 
				}

			}

		}

		System.out.println("Ŭ���̾�Ʈ ��������");
	}

	// �޽��� ���۽ó� ���������� ������ �߻��� �� ȣ��Ǵ� �޼ҵ�
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		System.out.println("���ۿ��� �߻�");
	}

	public void roomListupdate() throws Exception {
		// �����ڿ��� ������ ������ �����ϱ� ���� ���̽� ������Ʈ�� �迭�� �غ��Ѵ�.
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cmd", "roomList");
		JSONArray jsonArr = new JSONArray();

		// ������ ����� ���� �������� ����� Ű���� �޾Ƴ���
		Iterator<Integer> keys = listDataMap.keySet().iterator();
		while (keys.hasNext()) {
			int listkey = keys.next();

			// ����Ʈ�� ���� ���̽� ������Ʈ�� �����. �׸��� ���������� Ű���� ���� ��ȣ�̹Ƿ�
			// �ٷ� ������Ʈ�� ��´�
			JSONObject listData = new JSONObject();
			listData.put("rnum", listkey);

			// �������� ��Ͽ��� ������ ���� Ű���� �̿��� �������� �� �ȿ��� ���� ������
			// �̾Ƴ��� ���̽� ������Ʈ�� ��´�
			Map<String, String> DataMap = listDataMap.get(listkey);
			String roomName = DataMap.get("roomName");
			listData.put("roomName", roomName);

			// �ϼ��� ���̽� ������Ʈ�� �迭�� ��´�
			jsonArr.add(listData);
		}

		// ��ȯ�� ���̽� ������Ʈ�� �ϼ��� �迭�� ��� ���ڿ��� �����
		jsonObj.put("list", jsonArr);
		String sJson = jsonObj.toJSONString();

		Iterator<String> sessionkeys = sessionMap.keySet().iterator();
		while (sessionkeys.hasNext()) {
			String sessionkey = sessionkeys.next();
			sessionMap.get(sessionkey).sendMessage(new TextMessage(sJson));
		}
	}

	public void memListupdate() throws Exception {
		// ���� �̿����� �̿��ڿ��� ���ŵ� ����Ʈ ����
		JSONObject jsonMem = new JSONObject();
		jsonMem.put("cmd", "memList");
		JSONArray memList = new JSONArray();

		Iterator<String> members = sessionMap.keySet().iterator();

		while (members.hasNext()) {
			String member = members.next();
			memList.add(member);
		}
		jsonMem.put("list", memList);

		String sJson = jsonMem.toJSONString();

		Iterator<String> sessionkeys = sessionMap.keySet().iterator();
		while (sessionkeys.hasNext()) {
			String sessionkey = sessionkeys.next();
			sessionMap.get(sessionkey).sendMessage(new TextMessage(sJson));
		}

		Iterator<Integer> roomkeys = listMemberMap.keySet().iterator();
		while (roomkeys.hasNext()) {
			// Ű���� ����ؼ� ����Ʈ ����� ������ �� ���� �г��Ӱ� ������ ��� ���� �ҷ�����.
			Map<String, WebSocketSession> roomMemberMap = listMemberMap.get(roomkeys.next());

			Iterator<String> roomkeys2 = roomMemberMap.keySet().iterator();
			while (roomkeys2.hasNext()) {
				String key2 = roomkeys2.next();
				System.out.println(sJson);
				roomMemberMap.get(key2).sendMessage(new TextMessage(sJson));
			}

		}
	}

	private void roomUpdate(Map<String, WebSocketSession> roomMemberMap, Map<String, String> roomDataMap)
			throws Exception {

		// �� ���� �ο����� �� ������ �����Ѵ�.
		JSONObject roomData = new JSONObject();
		roomData.put("cmd", "roomUpdate");

		JSONArray roomMembers = new JSONArray();
		Iterator<String> roomMemNames = roomMemberMap.keySet().iterator();
		while (roomMemNames.hasNext()) {
			String key = roomMemNames.next();
			roomMembers.add(key);
		}
		roomData.put("roomMembers", roomMembers);

		JSONArray memList = new JSONArray();
		Iterator<String> members = sessionMap.keySet().iterator();
		while (members.hasNext()) {
			String member = members.next();
			memList.add(member);
		}
		roomData.put("list", memList);
		roomData.put("master", roomDataMap.get("master"));
		roomData.put("memCnt", roomDataMap.get("memCnt"));

		String sJson = roomData.toJSONString();

		Iterator<String> roomMemberKeys = roomMemberMap.keySet().iterator();
		while (roomMemberKeys.hasNext()) {
			String key = roomMemberKeys.next();
			roomMemberMap.get(key).sendMessage(new TextMessage(sJson));
			System.out.println(key + "���� ���� �Ϸ�");
		}
		System.out.println("�� ������Ʈ �Ϸ�");

	}
}