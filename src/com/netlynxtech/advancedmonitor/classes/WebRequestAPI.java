package com.netlynxtech.advancedmonitor.classes;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;

public class WebRequestAPI {
	Context context;
	ArrayList<String> locationList = new ArrayList<String>();

	public WebRequestAPI(Context context) {
		this.context = context;
	}

	public ArrayList<String> getLocationList() {
		return locationList;
	}

	public String UpdateLocation(String deviceID, String latitude, String longitude) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_UPDATELOCATION_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceID);
		rpc.addProperty("latitude", latitude);
		rpc.addProperty("longitude", longitude);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL, Consts.WEBREQUEST_TIMEOUT); // 20 seconds timeout
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_UPDATELOCATION_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return "failed";
	}

	public ArrayList<String> GetLocation(String deviceId) {
		ArrayList<String> loc = new ArrayList<String>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETLOCATION_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "GetLocation");
			ht.call(Consts.NOISELYNX_API_GETLOCATION_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				Log.e("Latitude", result.getProperty("Latitude").toString());
				loc.add(result.getProperty("Latitude").toString());
				loc.add(result.getProperty("Longitude").toString());
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return loc;
	}

	public String DeleteDevice(String deviceId) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_DELETEDEVICE_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "DeleteDevice");
			ht.call(Consts.NOISELYNX_API_DELETEDEVICE_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String UpdateHumidityThreshold(String deviceId, String hiThreshold, String lowThreshold) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_UPDATEHUMIDITYTHRESHOLD_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		rpc.addProperty("thresholdHi", hiThreshold);
		rpc.addProperty("thresholdLo", lowThreshold);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "UpdateHumidityThreshold");
			ht.call(Consts.NOISELYNX_API_UPDATEHUMIDITYTHRESHOLD_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String UpdateTemperatureThreshold(String deviceId, String hiThreshold, String lowThreshold) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_UPDATETEMPERATURETHRESHOLD_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		rpc.addProperty("thresholdHi", hiThreshold);
		rpc.addProperty("thresholdLo", lowThreshold);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "UpdateTemperatureThreshold");
			ht.call(Consts.NOISELYNX_API_UPDATETEMPERATURETHRESHOLD_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String AckMessage(String eventId, String message) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_ACKMESSAGE_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		// Log.e("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("eventID", eventId); // set parameter
		rpc.addProperty("message", message); // set parameter
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "AckMessage");
			ht.call(Consts.NOISELYNX_API_ACKMESSAGE_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public ArrayList<HashMap<String, String>> GetMessages() {
		SQLFunctions sql = new SQLFunctions(context);
		sql.open();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETMESSAGES_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETMESSAGES_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();

			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				Message d = new Message();
				d.setMessageId(object.getProperty(Consts.MESSAGES_MESSAGE_ID).toString());
				d.setEventId(object.getProperty(Consts.MESSAGES_MESSAGE_EVENTID).toString());
				d.setTitle(object.getProperty(Consts.MESSAGES_MESSAGE_TITLE).toString());
				d.setTimestamp(object.getProperty(Consts.MESSAGES_MESSAGE_TIMESTAMP).toString());
				d.setDeviceId(object.getProperty(Consts.MESSAGES_MESSAGE_DEVICEID).toString());
				d.setMessageType(object.getProperty(Consts.MESSAGES_MESSAGE_MESSAGETYPE).toString());
				d.setMessage(object.getProperty(Consts.MESSAGES_MESSAGE_MESSAGE).toString());
				d.setAckRequired(object.getProperty(Consts.MESSAGES_MESSAGE_ACKREQUIRED).toString());
				// list.add(d);
				sql.insertMessage(d);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		list = sql.loadEventMessage();
		sql.close();
		return list;
	}

	public ArrayList<DeviceMembers> GetMembers(String deviceId) {
		ArrayList<DeviceMembers> list = new ArrayList<DeviceMembers>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETMEMBERS_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETMEMBERS_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();

			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				DeviceMembers d = new DeviceMembers();
				d.setName(object.getProperty(Consts.NAME).toString());
				d.setRole(object.getProperty(Consts.ROLE).toString());
				d.setRequestStatus(object.getProperty(Consts.REQUESTSTATUS).toString());
				d.setUpdateTimestamp(object.getProperty(Consts.UPDATETIMESTAMP).toString());
				d.setUdid(object.getProperty(Consts.UDID).toString());
				list.add(d);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}

	public ArrayList<DeviceRequest> ListMemberRequests() {
		ArrayList<DeviceRequest> list = new ArrayList<DeviceRequest>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_LISTMEMBERREQUESTS_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_LISTMEMBERREQUESTS_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();

			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				DeviceRequest d = new DeviceRequest();
				d.setDeviceId(object.getProperty(Consts.GETDEVICES_DEVICEID).toString());
				d.setDeviceName(object.getProperty(Consts.GETDEVICES_DEVICENAME).toString());
				Log.e("DEVICENAME", object.getProperty(Consts.GETDEVICES_DEVICENAME).toString());
				d.setRequestorName(object.getProperty(Consts.REQUESTORNAME).toString());
				d.setRole(object.getProperty(Consts.ROLE).toString());
				d.setTimestamp(object.getProperty(Consts.REQUESTORTIMESTAMP).toString());
				list.add(d);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}

	public ArrayList<HashMap<String, String>> GetChartData(String deviceId, String dateFrom, String dateTo, int slot) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETCHARTDATA_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		rpc.addProperty("dateFrom", dateFrom);
		rpc.addProperty("dateTo", dateTo);
		rpc.addProperty("numSlots", slot);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETCHARTDATA_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			HashMap<String, String> map;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				map = new HashMap<String, String>();
				map.put(Consts.GETDEVICES_TEMPERATURE, object.getProperty(Consts.GETDEVICES_TEMPERATURE).toString());
				map.put(Consts.GETDEVICES_HUMIDITY, object.getProperty(Consts.GETDEVICES_HUMIDITY).toString());
				map.put(Consts.GETDEVICES_DATATIMESTAMP, Utils.parseTimeToMinutesOnly(object.getProperty(Consts.GETDEVICES_DATATIMESTAMP).toString()));
				Log.e("DATATIMESTAMP!!!", Utils.parseTimeToMinutesOnly(object.getProperty(Consts.GETDEVICES_DATATIMESTAMP).toString()));
				list.add(map);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}

	public String RespondToMemberRequest(String deviceId, String responseType, String remarks) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_RESPONDTOMEMBERREQUEST_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		Log.e("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId); // set parameter
		rpc.addProperty("requestResponseType", responseType); // set parameter
		rpc.addProperty("remarks", remarks); // set parameter
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "RespondToMemberRequest");
			ht.call(Consts.NOISELYNX_API_RESPONDTOMEMBERREQUEST_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String RemoveMemberFromDevice(String deviceId, String requesterUDID) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_REMOVEMEMBERFROMDEVICE_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", requesterUDID);
		rpc.addProperty("deviceID", deviceId); // set parameter
		rpc.addProperty("requesterUDID", new Utils(context).getDeviceUniqueId()); // set parameter
		Log.e("requesterUDID", requesterUDID);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "RespondToMemberRequest");
			ht.call(Consts.NOISELYNX_API_REMOVEMEMBERFROMDEVICE_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String RequestPin(String mobileNo, String name) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_REQUESTPIN_METHOD_NAME); // create new soap object
		rpc.addProperty("handphone", mobileNo); // set parameter
		rpc.addProperty("deviceType", 1); // set parameter
		rpc.addProperty("name", name); // set parameter
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "RegisterUser");
			ht.call(Consts.NOISELYNX_API_REQUESTPIN_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String AssignMemberToDevice(String mobileNo, String deviceId, String role) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_ASSIGNMEMBERTODEVICEN_METHOD_NAME); // create new soap object
		rpc.addProperty("handphone", mobileNo); // set parameter
		rpc.addProperty("deviceID", deviceId); // set parameter
		rpc.addProperty("role", Integer.parseInt(role)); // set parameter
		rpc.addProperty("requesterUDID", new Utils(context).getDeviceUniqueId()); // set parameter
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			Log.e("WebRequest", "AssignMemberToDevice");
			ht.call(Consts.NOISELYNX_API_ASSIGNMEMBERTODEVICE_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String VerifyPin(String pin, String gcmid) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_VERIFYPIN_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId()); // set parameter
		rpc.addProperty("handphone", new Utils(context).getHandphoneNumber()); // set parameter
		rpc.addProperty("PIN", pin.trim()); // set parameter
		rpc.addProperty("GCMID", gcmid); // set parameter
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_VERIFYPIN_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			Log.e("RESULT", result.toString());
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String requestPin() {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_REGISTERDEVICE_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId()); // set parameter
		Log.e("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", "51235000031111"); // set parameter
		Log.e("DEVICEID", "51235000031111");
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL); // set base link
		ht.debug = true;
		try {
			// //Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_REGISTERDEVICE_SOAP_ACTION, envelope); // call web request
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse(); // get response
			// Log.e("RESULT", result.toString());
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String RegisterDevice(String deviceId) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_REGISTERDEVICE_METHOD_NAME); // create new soap object
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId()); // set parameter
		Log.e("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId); // set parameter
		Log.e("DEVICEID", deviceId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		Log.e("Calling to", Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			ht.call(Consts.NOISELYNX_API_REGISTERDEVICE_SOAP_ACTION, envelope);
			Log.e("SoapAction", Consts.NOISELYNX_API_REGISTERDEVICE_SOAP_ACTION);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String checkPin(String mobileNumber, String pin, String gcm_id, String udid) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_CHECKPIN_METHOD_NAME);
		rpc.addProperty("mobile_no", mobileNumber);
		rpc.addProperty("pin", pin);
		rpc.addProperty("GCM_ID", gcm_id);
		rpc.addProperty("UDID", udid);
		// Log.e("TEST", mobileNumber + "|" + pin + "|" + gcm_id + "|" + udid);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_CHECKPIN_SOAP_ACTION, envelope);
			// System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success|" + result.getProperty(1).toString();
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public ArrayList<Device> GetDevices() {
		ArrayList<Device> list = new ArrayList<Device>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETDEVICES_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			ht.call(Consts.NOISELYNX_API_GETDEVICES_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			Device map;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				map = new Device();
				map.setVersion(object.getProperty(Consts.GETDEVICES_VERSION).toString());
				map.setDeviceID(object.getProperty(Consts.GETDEVICES_DEVICEID).toString());
				map.setTemperature(object.getProperty(Consts.GETDEVICES_TEMPERATURE).toString());
				map.setHumidity(object.getProperty(Consts.GETDEVICES_HUMIDITY).toString());
				map.setVoltage(object.getProperty(Consts.GETDEVICES_VOLTAGE).toString());
				map.setInput1(object.getProperty(Consts.GETDEVICES_INPUT1).toString());
				map.setInput2(object.getProperty(Consts.GETDEVICES_INPUT2).toString());
				map.setInput3(object.getProperty(Consts.GETDEVICES_INPUT3).toString());
				map.setInput4(object.getProperty(Consts.GETDEVICES_INPUT4).toString());
				map.setInput5(object.getProperty(Consts.GETDEVICES_INPUT5).toString());
				map.setInput6(object.getProperty(Consts.GETDEVICES_INPUT6).toString());
				map.setInput7(object.getProperty(Consts.GETDEVICES_INPUT7).toString());
				map.setInput8(object.getProperty(Consts.GETDEVICES_INPUT8).toString());

				map.setOutput1(object.getProperty(Consts.GETDEVICES_OUTPUT1).toString());
				map.setOutput2(object.getProperty(Consts.GETDEVICES_OUTPUT2).toString());
				map.setEnableTemperature(object.getProperty(Consts.GETDEVICES_ENABLETEMPERATURE).toString());
				map.setEnableHumidity(object.getProperty(Consts.GETDEVICES_ENABLEHUMIDITY).toString());
				map.setEnableInput1(object.getProperty(Consts.GETDEVICES_ENABLEINPUT1).toString());
				map.setEnableInput2(object.getProperty(Consts.GETDEVICES_ENABLEINPUT2).toString());
				map.setEnableInput3(object.getProperty(Consts.GETDEVICES_ENABLEINPUT3).toString());
				map.setEnableInput4(object.getProperty(Consts.GETDEVICES_ENABLEINPUT4).toString());
				map.setEnableInput5(object.getProperty(Consts.GETDEVICES_ENABLEINPUT5).toString());
				map.setEnableInput6(object.getProperty(Consts.GETDEVICES_ENABLEINPUT6).toString());
				map.setEnableInput7(object.getProperty(Consts.GETDEVICES_ENABLEINPUT7).toString());
				map.setEnableInput8(object.getProperty(Consts.GETDEVICES_ENABLEINPUT8).toString());

				map.setEnableOutput1(object.getProperty(Consts.GETDEVICES_ENABLEOUTPUT1).toString());
				map.setEnableOutput2(object.getProperty(Consts.GETDEVICES_ENABLEOUTPUT2).toString());
				Log.e("TIME", object.getProperty(Consts.GETDEVICES_TIMESTAMP).toString());
				map.setTimestamp(object.getProperty(Consts.GETDEVICES_TIMESTAMP).toString());

				map.setDescription(object.getProperty(Consts.GETDEVICES_DESCRIPTION).toString());
				map.setDescriptionInput1(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT1).toString());
				map.setDescriptionInput2(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT2).toString());
				map.setDescriptionInput3(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT3).toString());
				map.setDescriptionInput4(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT4).toString());
				map.setDescriptionInput5(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT5).toString());
				map.setDescriptionInput6(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT6).toString());
				map.setDescriptionInput7(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT7).toString());
				map.setDescriptionInput8(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT8).toString());
				map.setDescriptionOutput1(object.getProperty(Consts.GETDEVICES_DESCRIPTIONOUTPUT1).toString());
				map.setDescriptionOutput2(object.getProperty(Consts.GETDEVICES_DESCRIPTIONOUTPUT2).toString());
				map.setTemperatureHi(object.getProperty(Consts.GETDEVICES_TEMPERATUREHI).toString());
				map.setTemperatureLo(object.getProperty(Consts.GETDEVICES_TEMPERATURELO).toString());
				map.setHumidityHi(object.getProperty(Consts.GETDEVICES_HUMIDITYHI).toString());
				map.setHumidityLo(object.getProperty(Consts.GETDEVICES_HUMIDITYLO).toString());
				map.setReverseLogicInput1(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT1).toString());
				map.setReverseLogicInput2(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT2).toString());
				map.setReverseLogicInput3(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT3).toString());
				map.setReverseLogicInput4(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT4).toString());
				map.setReverseLogicInput5(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT5).toString());
				map.setReverseLogicInput6(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT6).toString());
				map.setReverseLogicInput7(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT7).toString());
				map.setReverseLogicInput8(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT8).toString());
				map.setReverseLogicOutput1(object.getProperty(Consts.GETDEVICES_REVERSELOGICOUTPUT1).toString());
				map.setReverseLogicOutput2(object.getProperty(Consts.GETDEVICES_REVERSELOGICOUTPUT2).toString());
				map.setHumidityState(object.getProperty(Consts.GETDEVICES_HUMIDITYSTATE).toString());
				map.setTemperatureState(object.getProperty(Consts.GETDEVICES_TEMPERATURESTATE).toString());
				map.setLatitude(object.getProperty(Consts.GETDEVICES_LATITUDE).toString());
				map.setLongitude(object.getProperty(Consts.GETDEVICES_LONGITUDE).toString());
				map.setRole(object.getProperty(Consts.ROLE).toString());
				list.add(map);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}

	public Device GetDevice(String deviceId) {
		Device map = new Device();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETDEVICE_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		Log.e("GetDevice", deviceId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			ht.call(Consts.NOISELYNX_API_GETDEVICE_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject object = (SoapObject) envelope.getResponse();
			map.setVersion(object.getProperty(Consts.GETDEVICES_VERSION).toString());
			map.setDeviceID(object.getProperty(Consts.GETDEVICES_DEVICEID).toString());
			map.setTemperature(object.getProperty(Consts.GETDEVICES_TEMPERATURE).toString());
			map.setHumidity(object.getProperty(Consts.GETDEVICES_HUMIDITY).toString());
			map.setVoltage(object.getProperty(Consts.GETDEVICES_VOLTAGE).toString());
			map.setInput1(object.getProperty(Consts.GETDEVICES_INPUT1).toString());
			map.setInput2(object.getProperty(Consts.GETDEVICES_INPUT2).toString());
			map.setInput3(object.getProperty(Consts.GETDEVICES_INPUT3).toString());
			map.setInput4(object.getProperty(Consts.GETDEVICES_INPUT4).toString());
			map.setInput5(object.getProperty(Consts.GETDEVICES_INPUT5).toString());
			map.setInput6(object.getProperty(Consts.GETDEVICES_INPUT6).toString());
			map.setInput7(object.getProperty(Consts.GETDEVICES_INPUT7).toString());
			map.setInput8(object.getProperty(Consts.GETDEVICES_INPUT8).toString());

			map.setOutput1(object.getProperty(Consts.GETDEVICES_OUTPUT1).toString());
			map.setOutput2(object.getProperty(Consts.GETDEVICES_OUTPUT2).toString());
			map.setEnableTemperature(object.getProperty(Consts.GETDEVICES_ENABLETEMPERATURE).toString());
			map.setEnableHumidity(object.getProperty(Consts.GETDEVICES_ENABLEHUMIDITY).toString());
			map.setEnableInput1(object.getProperty(Consts.GETDEVICES_ENABLEINPUT1).toString());
			map.setEnableInput2(object.getProperty(Consts.GETDEVICES_ENABLEINPUT2).toString());
			map.setEnableInput3(object.getProperty(Consts.GETDEVICES_ENABLEINPUT3).toString());
			map.setEnableInput4(object.getProperty(Consts.GETDEVICES_ENABLEINPUT4).toString());
			map.setEnableInput5(object.getProperty(Consts.GETDEVICES_ENABLEINPUT5).toString());
			map.setEnableInput6(object.getProperty(Consts.GETDEVICES_ENABLEINPUT6).toString());
			map.setEnableInput7(object.getProperty(Consts.GETDEVICES_ENABLEINPUT7).toString());
			map.setEnableInput8(object.getProperty(Consts.GETDEVICES_ENABLEINPUT8).toString());

			map.setEnableOutput1(object.getProperty(Consts.GETDEVICES_ENABLEOUTPUT1).toString());
			map.setEnableOutput2(object.getProperty(Consts.GETDEVICES_ENABLEOUTPUT2).toString());
			Log.e("TIME", object.getProperty(Consts.GETDEVICES_TIMESTAMP).toString());
			map.setTimestamp(object.getProperty(Consts.GETDEVICES_TIMESTAMP).toString());

			map.setDescription(object.getProperty(Consts.GETDEVICES_DESCRIPTION).toString());
			map.setDescriptionInput1(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT1).toString());
			map.setDescriptionInput2(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT2).toString());
			map.setDescriptionInput3(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT3).toString());
			map.setDescriptionInput4(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT4).toString());
			map.setDescriptionInput5(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT5).toString());
			map.setDescriptionInput6(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT6).toString());
			map.setDescriptionInput7(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT7).toString());
			map.setDescriptionInput8(object.getProperty(Consts.GETDEVICES_DESCRIPTIONINPUT8).toString());
			map.setDescriptionOutput1(object.getProperty(Consts.GETDEVICES_DESCRIPTIONOUTPUT1).toString());
			map.setDescriptionOutput2(object.getProperty(Consts.GETDEVICES_DESCRIPTIONOUTPUT2).toString());
			map.setTemperatureHi(object.getProperty(Consts.GETDEVICES_TEMPERATUREHI).toString());
			map.setTemperatureLo(object.getProperty(Consts.GETDEVICES_TEMPERATURELO).toString());
			map.setHumidityHi(object.getProperty(Consts.GETDEVICES_HUMIDITYHI).toString());
			map.setHumidityLo(object.getProperty(Consts.GETDEVICES_HUMIDITYLO).toString());
			map.setReverseLogicInput1(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT1).toString());
			map.setReverseLogicInput2(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT2).toString());
			map.setReverseLogicInput3(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT3).toString());
			map.setReverseLogicInput4(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT4).toString());
			map.setReverseLogicInput5(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT5).toString());
			map.setReverseLogicInput6(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT6).toString());
			map.setReverseLogicInput7(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT7).toString());
			map.setReverseLogicInput8(object.getProperty(Consts.GETDEVICES_REVERSELOGICINPUT8).toString());
			map.setReverseLogicOutput1(object.getProperty(Consts.GETDEVICES_REVERSELOGICOUTPUT1).toString());
			map.setReverseLogicOutput2(object.getProperty(Consts.GETDEVICES_REVERSELOGICOUTPUT2).toString());
			map.setHumidityState(object.getProperty(Consts.GETDEVICES_HUMIDITYSTATE).toString());
			map.setTemperatureState(object.getProperty(Consts.GETDEVICES_TEMPERATURESTATE).toString());
			map.setLatitude(object.getProperty(Consts.GETDEVICES_LATITUDE).toString());
			map.setLongitude(object.getProperty(Consts.GETDEVICES_LONGITUDE).toString());
			map.setRole(object.getProperty(Consts.ROLE).toString());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return map;
	}

	public String SetOutput(String deviceId, String outputNum, String onOff) {
		Log.e("DATA", deviceId + "|" + outputNum + "|" + onOff);
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_SETOUTPUT_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		rpc.addProperty("outputNum", outputNum);
		rpc.addProperty("OnOff", onOff);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_SETOUTPUT_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success|" + result.getProperty(1).toString();
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String UpdateDescriptions(String deviceId, String description, String input1, String input2, String input3, String input4, String input5, String input6, String input7, String input8,
			String output1, String output2) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_UPDATEDESCRIPTIONS_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceUniqueId());
		rpc.addProperty("deviceID", deviceId);
		rpc.addProperty("description", description);
		rpc.addProperty("descriptionInput1", input1);
		rpc.addProperty("descriptionInput2", input2);
		rpc.addProperty("descriptionInput3", input3);
		rpc.addProperty("descriptionInput4", input4);
		rpc.addProperty("descriptionInput5", input5);
		rpc.addProperty("descriptionInput6", input6);
		rpc.addProperty("descriptionInput7", input7);
		rpc.addProperty("descriptionInput8", input8);
		rpc.addProperty("descriptionOutput1", output1);
		rpc.addProperty("descriptionOutput2", output2);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_UPDATEDESCRIPTIONS_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success|" + result.getProperty(1).toString();
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public ArrayList<HashMap<String, String>> getDevices(String udid) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETDEVICES_METHOD_NAME);
		rpc.addProperty("UDID", udid);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE("http://210.193.23.250/wsNoiseLynx/noiselynx.asmx");
		ht.debug = true;
		try {
			// Log.e("WebRequest", "TRY!");
			ht.call("http://NetlynxTech.com/GetDevices", envelope);
			// System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			HashMap<String, String> map;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				map = new HashMap<String, String>();
				// Log.e("Location", object.getProperty(Consts.MONITORING_LOCATION) + "");
				locationList.add(object.getProperty(Consts.MONITORING_LOCATION).toString());
				map.put(Consts.MONITORING_DEVICE_ID, object.getProperty(Consts.MONITORING_DEVICE_ID).toString());
				map.put(Consts.MONITORING_DATE_TIME, object.getProperty(Consts.MONITORING_DATE_TIME).toString());
				map.put(Consts.MONITORING_LOCATION, object.getProperty(Consts.MONITORING_LOCATION).toString());
				map.put(Consts.MONITORING_LEQ_FIVE_MINUTES, object.getProperty(Consts.MONITORING_LEQ_FIVE_MINUTES).toString());
				map.put(Consts.MONITORING_LEQ_ONE_HOUR, object.getProperty(Consts.MONITORING_LEQ_ONE_HOUR).toString());
				map.put(Consts.MONITORING_LEQ_TWELVE_HOUR, object.getProperty(Consts.MONITORING_LEQ_TWELVE_HOUR).toString());
				map.put(Consts.MONITORING_LOCATION_LAT, object.getProperty(Consts.MONITORING_LOCATION_LAT).toString());
				map.put(Consts.MONITORING_LOCATION_LONG, object.getProperty(Consts.MONITORING_LOCATION_LONG).toString());
				map.put(Consts.MONITORING_ALERT, object.getProperty(Consts.MONITORING_ALERT).toString());
				Log.e("ALERT", object.getProperty(Consts.MONITORING_ALERT).toString());
				list.add(map);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();

		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}
}
