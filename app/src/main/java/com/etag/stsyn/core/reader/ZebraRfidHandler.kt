package com.etag.stsyn.core.reader

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.etag.m003ams.common.reader.core.DWInterface
import com.zebra.rfid.api3.Antennas
import com.zebra.rfid.api3.ENUM_TRANSPORT
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE
import com.zebra.rfid.api3.INVENTORY_STATE
import com.zebra.rfid.api3.InvalidUsageException
import com.zebra.rfid.api3.OperationFailureException
import com.zebra.rfid.api3.RFIDReader
import com.zebra.rfid.api3.ReaderDevice
import com.zebra.rfid.api3.Readers
import com.zebra.rfid.api3.RfidEventsListener
import com.zebra.rfid.api3.RfidReadEvents
import com.zebra.rfid.api3.RfidStatusEvents
import com.zebra.rfid.api3.SESSION
import com.zebra.rfid.api3.SL_FLAG
import com.zebra.rfid.api3.START_TRIGGER_TYPE
import com.zebra.rfid.api3.STATUS_EVENT_TYPE
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE
import com.zebra.rfid.api3.TagData
import com.zebra.rfid.api3.TriggerInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ZebraRfidHandler @Inject constructor(
    @ApplicationContext val application: Application,
) : RfidEventsListener, Readers.RFIDReaderEventHandler {

    companion object {
        const val MAX_POWER = 300
        const val DEFAULT_POWER = 100
    }

    //region Properties
    // UI and context
    private var context: Context? = null
    private val dwInterface = DWInterface()
    private var mRfidResponseHandlerInterface: RfidResponseHandlerInterface? = null
    private var availableRFIDReaderList: MutableList<ReaderDevice> = mutableListOf()
    private var mConnectedReader: RFIDReader? = null
    private var readerDevice: ReaderDevice? = null
    var antennaRfConfig: Antennas.AntennaRfConfig? = null

    private var onBatteryLevelListener: RfidBatteryLevelListener? = null
    var readers: Readers? = null
    val isReaderConnected: Boolean
        get() {
            return mConnectedReader != null && mConnectedReader!!.isConnected
        }
    //endregion

    //region Method

    fun setOnBatteryLevelListener(rfidBatteryLevelListener: RfidBatteryLevelListener) {
        this.onBatteryLevelListener = rfidBatteryLevelListener
    }

    suspend fun onCreate(): Job {
        // application context
        Log.e("ASDASD", "CONNECT")
        context = application
        // SDK
        return withContext(Dispatchers.IO) {
            async {
                initSDK()
            }
        }
    }

    //
    // RFID SDK
    private suspend fun initSDK() {
        if (readers == null) {
            createInstanceTask()
        } else {
            connectionTaskAsync()
        }
    }

    private suspend fun createInstanceTask() {
        readers = Readers(context, ENUM_TRANSPORT.BLUETOOTH) // ALL
        connectionTaskAsync()
        availableRFIDReaderList = readers!!.GetAvailableRFIDReaderList()
        readers!!.Dispose()
        readers = null
        if (readers == null) {
            readers = Readers(context, ENUM_TRANSPORT.BLUETOOTH) // ALL
        }
    }

    suspend fun connectionTaskAsync(readerDevice: ReaderDevice? = null) {

        if (readerDevice == null) {
            getAvailableReader()
        } else {
            mConnectedReader = readerDevice.rfidReader
        }

        if (mConnectedReader != null) {
            connect()
        } else {
            "Failed to find or connect reader"
        }

//        // it works
//        getAvailableReader()
//        if (mConnectedReader != null) {
//            connect()
//        } else {
//
//        }

    }


    private suspend fun getAvailableReader() {
        withContext(Dispatchers.IO) {
            async {
                if (readers != null) {
                    if (readers!!.GetAvailableRFIDReaderList() != null) {
                        availableRFIDReaderList = readers!!.GetAvailableRFIDReaderList()
                        if (availableRFIDReaderList.isNotEmpty()) {
                            readerDevice = availableRFIDReaderList[0]
                            mConnectedReader = readerDevice!!.rfidReader
                        }
                    }
                }
            }.await()
        }
    }

    private suspend fun connect() {
        try {
            if (mConnectedReader != null) {
                if (!mConnectedReader!!.isConnected) {
                    // Establish connection to the RFID Reader
                    withContext(Dispatchers.IO) {
                        async {
                            mConnectedReader!!.connect()
                            configureReader(mConnectedReader!!)
                            mRfidResponseHandlerInterface?.handleReaderConnected(true)
                        }
                    }
//                    startTimer()
//                    setReaderConnect(true)
                } else {
                    // "Already Connected"
                }
            }
        } catch (e: InvalidUsageException) {
            e.printStackTrace()
//                setReaderConnect(false)
            mRfidResponseHandlerInterface?.handleReaderConnected(false)
            e.vendorMessage
        } catch (e: OperationFailureException) {
            e.printStackTrace()
//                setReaderConnect(false)
            val des = e.results.toString()
            mRfidResponseHandlerInterface?.handleReaderConnected(false)
            "Connection failed" + e.vendorMessage + " " + des
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            async {
                try {
                    if (mConnectedReader != null) {
                        mConnectedReader!!.disconnect()
                        mConnectedReader!!.Events.removeEventsListener(this@ZebraRfidHandler)
//                stopTimer()
//                setReaderConnect(false)
                        //  dispose()
                        mRfidResponseHandlerInterface?.handleReaderConnected(false)
                    } else {
                        mRfidResponseHandlerInterface?.handleReaderConnected(false)
                    }
                } catch (e: InvalidUsageException) {
//            setReaderConnect(false)
                    mRfidResponseHandlerInterface?.handleReaderConnected(false)
                    e.printStackTrace()
                } catch (e: OperationFailureException) {
//            setReaderConnect(false)
                    mRfidResponseHandlerInterface?.handleReaderConnected(false)
                    e.printStackTrace()
                } catch (e: Exception) {
//            setReaderConnect(false)
                    mRfidResponseHandlerInterface?.handleReaderConnected(false)
                    e.printStackTrace()
                }
            }.await()
        }
    }


    suspend fun dispose() {
        try {
            if (readers != null) {
                disconnect()
                mConnectedReader = null
                readers!!.Dispose()
                readers = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun configureReader(mConnectedReader: RFIDReader) {
        if (mConnectedReader.isConnected) {
            try {
                mConnectedReader.apply {
                    // receive events from reader
                    Events.addEventsListener(this@ZebraRfidHandler)
                    // HH event
                    Events.setHandheldEvent(true)
                    // tag event with tag data
                    Events.setTagReadEvent(true)
                    Events.setAttachTagDataWithReadEvent(false)
                    // set trigger mode as rfid so scanner beam will not come
                    Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true)
                    Events.setBatteryEvent(true)
                    Events.setReaderDisconnectEvent(true)
                    Events.setBatchModeEvent(true)
                    // set start and stop triggers
                    val triggerInfo = TriggerInfo()
                    triggerInfo.StartTrigger.triggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_PERIODIC
                    triggerInfo.StopTrigger.triggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE
                    Config.startTrigger = triggerInfo.StartTrigger
                    Config.stopTrigger = triggerInfo.StopTrigger
                    // delete any prefilters
                    Actions.PreFilters.deleteAll()
                }
                setAntennaRfidReader(mConnectedReader)
                setSingulationControl(mConnectedReader)
                getBatteryLevel(mConnectedReader)
            } catch (e: InvalidUsageException) {
                e.printStackTrace()
            } catch (e: OperationFailureException) {
                e.printStackTrace()
            }
        }
    }

    private fun setSingulationControl(mConnectedReader: RFIDReader) {
        // Set the singulation control
        val s1SingulationControl = mConnectedReader.Config.Antennas.getSingulationControl(1).apply {
            session = SESSION.SESSION_S1
            Action.inventoryState = INVENTORY_STATE.INVENTORY_STATE_A
            Action.slFlag = SL_FLAG.SL_ALL
        }
        mConnectedReader.Config.Antennas.setSingulationControl(1, s1SingulationControl)
    }

    private fun setAntennaRfidReader(mConnectedReader: RFIDReader) {
        // set antenna configurations
        antennaRfConfig = mConnectedReader.Config.Antennas.getAntennaRfConfig(1).apply {
            setrfModeTableIndex(0)
            tari = 0
            transmitPowerIndex = 100
            //transmitPowerIndex = sharePreferenceHelper.readerPower ?: DEFAULT_POWER
        }
        mConnectedReader.Config.Antennas.setAntennaRfConfig(1, antennaRfConfig)
    }

    private fun getBatteryLevel(mConnectedReader: RFIDReader) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    mConnectedReader.Config.getDeviceStatus(
                        true, true, false
                    )
                } catch (e: Exception) {
                }
                delay(60000)
            }
        }
    }

    suspend fun setPowerLevel(powerLevel: Int): String {
        return withContext(Dispatchers.Default) {
            return@withContext if (!isReaderConnected) {
                "Reader Not connected"
            } else {
                try {
                    setAntennaRfidReader(mConnectedReader!!)
                    "Antenna power Set to $powerLevel"
                } catch (e: InvalidUsageException) {
                    "Failed to apply settings \nInvalid fields in antenna settings"
                } catch (e: OperationFailureException) {
                    e.printStackTrace()
                    "Failed to apply settings \nInvalid fields in antenna settings"
                }
            }
        }
    }


    suspend fun performInventory(): String {
        // check reader connection
        return withContext(Dispatchers.IO) {
            async {
                if (!isReaderConnected) return@async "Reader is not connect"
                return@async try {
                    mConnectedReader!!.Actions.purgeTags()
                    mConnectedReader!!.Actions.Inventory.perform()
                    "Start Scan"
                } catch (e: InvalidUsageException) {
                    e.printStackTrace()
                    e.toString()
                } catch (e: OperationFailureException) {
                    e.printStackTrace()
                    e.toString()
                }
            }.await()
        }
    }

    suspend fun stopInventory() {
        // check reader connection
        withContext(Dispatchers.IO) {
            async {
                if (!isReaderConnected) return@async
                try {
                    mConnectedReader!!.Actions.Inventory.stop()
                } catch (e: InvalidUsageException) {
                    e.printStackTrace()
                } catch (e: OperationFailureException) {
                    e.printStackTrace()
                }
            }.await()
        }
    }

    fun setResponseHandlerInterface(mRfidResponseHandlerInterface: RfidResponseHandlerInterface?) {
        this.mRfidResponseHandlerInterface = mRfidResponseHandlerInterface
    }

    fun removeResponseHandLerInterface() {
        this.mRfidResponseHandlerInterface = null
    }

    fun scanBarcode(): Boolean {
        dwInterface.sendCommandString(
            context!!, DWInterface.DATAWEDGE_SEND_SET_SOFT_SCAN, "START_SCANNING"
        )
        return false
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                dwInterface.sendCommandString(
//                    context!!, DWInterface.DATAWEDGE_SEND_SET_SOFT_SCAN, "START_SCANNING"
//                )
//                return true
//            }
//
//            MotionEvent.ACTION_UP -> {
//                dwInterface.sendCommandString(
//                    context!!, DWInterface.DATAWEDGE_SEND_SET_SOFT_SCAN, "STOP_SCANNING"
//                )
//                return true
//            }
//        }
//        return false
    }
//endregion

    //region Override Method
    override fun eventReadNotify(e: RfidReadEvents) {
        val myTags = mConnectedReader!!.Actions.getReadTags(1)

        if (myTags != null) {
            mRfidResponseHandlerInterface?.handleTagData(myTags)
        }
    }

    // Status Event Notification
    @SuppressLint("StaticFieldLeak")
    override fun eventStatusNotify(rfidStatusEvents: RfidStatusEvents) {

        println("powerData: ${rfidStatusEvents.StatusEventData.statusEventType}")
        if (rfidStatusEvents.StatusEventData.statusEventType === STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.handheldEvent == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                mRfidResponseHandlerInterface?.handleTriggerPress(true)
            }
            if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.handheldEvent === HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                mRfidResponseHandlerInterface?.handleTriggerPress(false)
            }
        } else if (rfidStatusEvents.StatusEventData.statusEventType === STATUS_EVENT_TYPE.BATTERY_EVENT) {
            val batteryData = rfidStatusEvents.StatusEventData.BatteryData

            onBatteryLevelListener?.onBatteryLevelChange(batteryData.level)

//            async { myDataStore.setBatteryLevel(batteryData.level) }
//            batterStatus?.batteryLevel(batteryData.level)
            Log.e("ASDASD", batteryData.level.toString())
        } else if (rfidStatusEvents.StatusEventData.statusEventType == STATUS_EVENT_TYPE.DISCONNECTION_EVENT) {
//            setReaderConnect(false)

            mRfidResponseHandlerInterface?.handleReaderConnected(false)
        }
    }

    // handler for receiving reader appearance events
    @OptIn(DelicateCoroutinesApi::class)
    override fun RFIDReaderAppeared(readerDevice: ReaderDevice) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                if (!isReaderConnected) {
                    connectionTaskAsync(readerDevice)
                }
            }
        }
    }

    override fun RFIDReaderDisappeared(p0: ReaderDevice?) {
        p0?.let {

        }
    }

//endregion


}


interface RfidResponseHandlerInterface {
    fun handleTagData(tagData: Array<TagData>)
    fun handleTriggerPress(pressed: Boolean) //void handleStatusEvents(Events.StatusEventData eventData);

    fun handleReaderConnected(isConnected: Boolean)
}

interface RfidBatteryLevelListener {
    fun onBatteryLevelChange(batteryLevel: Int)
}