package i2f.os.windows.perf;

import i2f.convert.Converters;
import i2f.os.OsUtil;
import i2f.os.windows.perf.data.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/30 10:46
 * @desc
 */
public class WindowsUtil {

    public static double getCpuLoadPercent() {
        double sum = 0;
        int count = 0;
        List<WindowsCpuDto> list = getCpuInfo();
        if (list == null || list.isEmpty()) {
            return -1;
        }
        for (WindowsCpuDto item : list) {
            sum += item.loadPercentage;
            count++;
        }
        return sum / count;
    }

    public static double getMemoryUsedPercent() {
        WindowsOsDto info = getOsInfo();
        if (info == null) {
            return -1;
        }
        return (info.totalVisibleMemorySize - info.freePhysicalMemory) * 1.0 / info.totalVisibleMemorySize * 100;
    }

    public static double getDiskUsedPercent() {
        double sum = 0;
        int count = 0;
        List<WindowsLogicalDiskDto> list = getLogicalDiskInfo();
        if (list == null || list.isEmpty()) {
            return -1;
        }
        for (WindowsLogicalDiskDto item : list) {
            double rate = (item.size - item.freeSpace) * 1.0 / item.size * 100.0;
            sum += rate;
            count++;
        }
        return sum / count;
    }

    public static double getDiskUsedPercent(char disk) {
        String name = (disk + ":");
        List<WindowsLogicalDiskDto> list = getLogicalDiskInfo();
        if (list == null || list.isEmpty()) {
            return -1;
        }
        for (WindowsLogicalDiskDto item : list) {
            if (name.equalsIgnoreCase(item.name)) {
                double rate = (item.size - item.freeSpace) * 1.0 / item.size * 100.0;
                return rate;
            }
        }
        return -1;
    }


    public static List<WindowsDesktopMonitorDto> getDesktopMonitorInfo() {
        List<WindowsDesktopMonitorDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("DESKTOPMONITOR");

        for (Map<String, String> map : list) {

            WindowsDesktopMonitorDto dto = new WindowsDesktopMonitorDto();

            dto.availability = map.get("Availability");
            dto.caption = map.get("Caption");
            dto.configManagerErrorCode = map.get("ConfigManagerErrorCode");
            dto.configManagerUserConfig = Converters.parseBoolean(map.get("ConfigManagerUserConfig"), false);
            dto.creationClassName = map.get("CreationClassName");
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.monitorManufacturer = map.get("MonitorManufacturer");
            dto.monitorType = map.get("MonitorType");
            dto.name = map.get("Name");
            dto.pixelsPerXLogicalInch = Converters.parseInt(map.get("PixelsPerXLogicalInch"), -1);
            dto.pixelsPerYLogicalInch = Converters.parseInt(map.get("PixelsPerYLogicalInch"), -1);
            dto.pnpDeviceId = map.get("PNPDeviceID");
            dto.status = map.get("Status");
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");

            ret.add(dto);
        }

        return ret;
    }


    public static List<WindowsPrinterConfigDto> getPrinterConfigInfo() {
        List<WindowsPrinterConfigDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }


        List<Map<String, String>> list = getWmicList("PRINTERCONFIG");

        for (Map<String, String> map : list) {

            WindowsPrinterConfigDto dto = new WindowsPrinterConfigDto();

            dto.caption = map.get("Caption");
            dto.collate = map.get("Collate");
            dto.color = map.get("Color");
            dto.copies = map.get("Copies");
            dto.description = map.get("Description");
            dto.deviceName = map.get("DeviceName");
            dto.driverVersion = map.get("DriverVersion");
            dto.duplex = map.get("Duplex");
            dto.formName = map.get("FormName");
            dto.horizontalResolution = Converters.parseInt(map.get("HorizontalResolution"), -1);
            dto.name = map.get("Name");
            dto.orientation = map.get("Orientation");
            dto.paperLength = Converters.parseInt(map.get("PaperLength"), -1);
            dto.paperSize = map.get("PaperSize");
            dto.paperWidth = Converters.parseInt(map.get("PaperWidth"), -1);
            dto.printQuality = Converters.parseInt(map.get("PrintQuality"), -1);
            dto.settingId = map.get("SettingID");
            dto.specificationVersion = map.get("SpecificationVersion");
            dto.ttOption = map.get("TTOption");
            dto.verticalResolution = Converters.parseInt(map.get("VerticalResolution"), -1);
            dto.xResolution = Converters.parseInt(map.get("XResolution"), -1);
            dto.yResolution = Converters.parseInt(map.get("YResolution"), -1);

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsPrinterDto> getPrinterInfo() {
        List<WindowsPrinterDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }


        List<Map<String, String>> list = getWmicList("PRINTER");

        for (Map<String, String> map : list) {

            WindowsPrinterDto dto = new WindowsPrinterDto();

            dto.attributes = map.get("Attributes");
            dto.averagePagesPerMinute = Converters.parseInt(map.get("AveragePagesPerMinute"), -1);
            dto.capabilities = map.get("Capabilities");
            dto.capabilityDescriptions = map.get("CapabilityDescriptions");
            dto.caption = map.get("Caption");
            dto.creationClassName = map.get("CreationClassName");
            dto.defaultUse = Converters.parseBoolean(map.get("Default"), false);
            dto.defaultPriority = Converters.parseInt(map.get("DefaultPriority"), -1);
            dto.detectedErrorState = map.get("DetectedErrorState");
            dto.deviceId = map.get("DeviceID");
            dto.direct = Converters.parseBoolean(map.get("Direct"), false);
            dto.doCompleteFirst = Converters.parseBoolean(map.get("DoCompleteFirst"), false);
            dto.driverName = map.get("DriverName");
            dto.enableBidi = Converters.parseBoolean(map.get("EnableBIDI"), false);
            dto.enableDevQueryPrint = Converters.parseBoolean(map.get("EnableDevQueryPrint"), false);
            dto.extendedDetectedErrorState = map.get("ExtendedDetectedErrorState");
            dto.extendedPrinterStatus = map.get("ExtendedPrinterStatus");
            dto.hidden = Converters.parseBoolean(map.get("Hidden"), false);
            dto.horizontalResolution = Converters.parseInt(map.get("HorizontalResolution"), -1);
            dto.jobCountSinceLastReset = Converters.parseInt(map.get("JobCountSinceLastReset"), -1);
            dto.keepPrintedJobs = Converters.parseBoolean(map.get("KeepPrintedJobs"), false);
            dto.languagesSupported = map.get("LanguagesSupported");
            dto.local = Converters.parseBoolean(map.get("Local"), false);
            dto.name = map.get("Name");
            dto.network = Converters.parseBoolean(map.get("Network"), false);
            dto.paperSizesSupported = map.get("PaperSizesSupported");
            dto.portName = map.get("PortName");
            dto.printerPaperNames = map.get("PrinterPaperNames");
            dto.printerState = map.get("PrinterState");
            dto.printerStatus = map.get("PrinterStatus");
            dto.printJobDataType = map.get("PrintJobDataType");
            dto.printProcessor = map.get("PrintProcessor");
            dto.priority = Converters.parseInt(map.get("Priority"), -1);
            dto.published = Converters.parseBoolean(map.get("Published"), false);
            dto.queued = Converters.parseBoolean(map.get("Queued"), false);
            dto.rawOnly = Converters.parseBoolean(map.get("RawOnly"), false);
            dto.shared = Converters.parseBoolean(map.get("Shared"), false);
            dto.spoolEnabled = Converters.parseBoolean(map.get("SpoolEnabled"), false);
            dto.status = map.get("Status");
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");
            dto.verticalResolution = map.get("VerticalResolution");
            dto.workOffline = Converters.parseBoolean(map.get("WorkOffline"), false);

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsStartupDto> getStartupInfo() {
        List<WindowsStartupDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }


        List<Map<String, String>> list = getWmicList("STARTUP");

        for (Map<String, String> map : list) {

            WindowsStartupDto dto = new WindowsStartupDto();

            dto.caption = map.get("Caption");
            dto.command = map.get("Command");
            dto.description = map.get("Description");
            dto.location = map.get("Location");
            dto.name = map.get("Name");
            dto.user = map.get("User");
            dto.userSid = map.get("UserSID");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsSoundDeviceDto> getSoundDeviceInfo() {
        List<WindowsSoundDeviceDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }


        List<Map<String, String>> list = getWmicList("SOUNDDEV");

        for (Map<String, String> map : list) {

            WindowsSoundDeviceDto dto = new WindowsSoundDeviceDto();

            dto.caption = map.get("Caption");
            dto.configManagerErrorCode = map.get("ConfigManagerErrorCode");
            dto.configManagerUserConfig = Converters.parseBoolean(map.get("ConfigManagerUserConfig"), false);
            dto.creationClassName = map.get("CreationClassName");
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.manufacturer = map.get("Manufacturer");
            dto.name = map.get("Name");
            dto.pnpDeviceId = map.get("PNPDeviceID");
            dto.powerManagementSupported = Converters.parseBoolean(map.get("PowerManagementSupported"), false);
            dto.productName = map.get("ProductName");
            dto.status = map.get("Status");
            dto.statusInfo = map.get("StatusInfo");
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsProcessDto> getProcessInfo() {
        List<WindowsProcessDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }


        List<Map<String, String>> list = getWmicList("PROCESS");

        for (Map<String, String> map : list) {

            WindowsProcessDto dto = new WindowsProcessDto();

            dto.caption = map.get("Caption");
            dto.commandLine = map.get("CommandLine");
            dto.creationClassName = map.get("CreationClassName");
            dto.creationDate = map.get("CreationDate");
            dto.csCreationClassName = map.get("CSCreationClassName");
            dto.csName = map.get("CSName");
            dto.description = map.get("Description");
            dto.executablePath = map.get("ExecutablePath");
            dto.handle = Converters.parseInt(map.get("Handle"), -1);
            dto.handleCount = Converters.parseInt(map.get("HandleCount"), -1);
            dto.kernelModeTime = Converters.parseLong(map.get("KernelModeTime"), -1);
            dto.maximumWorkingSetSize = Converters.parseLong(map.get("MaximumWorkingSetSize"), -1);
            dto.minimumWorkingSetSize = Converters.parseLong(map.get("MinimumWorkingSetSize"), -1);
            dto.name = map.get("Name");
            dto.osCreationClassName = map.get("OSCreationClassName");
            dto.osName = map.get("OSName");
            dto.otherOperationCount = Converters.parseInt(map.get("OtherOperationCount"), -1);
            dto.otherTransferCount = Converters.parseInt(map.get("OtherTransferCount"), -1);
            dto.pageFaults = Converters.parseLong(map.get("PageFaults"), -1);
            dto.pageFileUsage = Converters.parseLong(map.get("PageFileUsage"), -1);
            dto.parentProcessId = Converters.parseInt(map.get("ParentProcessId"), -1);
            dto.peakPageFileUsage = Converters.parseLong(map.get("PeakPageFileUsage"), -1);
            dto.peakVirtualSize = Converters.parseLong(map.get("PeakVirtualSize"), -1);
            dto.peakWorkingSetSize = Converters.parseLong(map.get("PeakWorkingSetSize"), -1);
            dto.priority = Converters.parseInt(map.get("Priority"), -1);
            dto.privatePageCount = Converters.parseLong(map.get("PrivatePageCount"), -1);
            dto.processId = Converters.parseInt(map.get("ProcessId"), -1);
            dto.quotaNonPagedPoolUsage = Converters.parseLong(map.get("QuotaNonPagedPoolUsage"), -1);
            dto.quotaPagedPoolUsage = Converters.parseLong(map.get("QuotaPagedPoolUsage"), -1);
            dto.quotaPeakNonPagedPoolUsage = Converters.parseLong(map.get("QuotaPeakNonPagedPoolUsage"), -1);
            dto.quotaPeakPagedPoolUsage = Converters.parseLong(map.get("QuotaPeakPagedPoolUsage"), -1);
            dto.readOperationCount = Converters.parseInt(map.get("ReadOperationCount"), -1);
            dto.readTransferCount = Converters.parseInt(map.get("ReadTransferCount"), -1);
            dto.sessionId = map.get("SessionId");
            dto.threadCount = Converters.parseInt(map.get("ThreadCount"), -1);
            dto.userModeTime = Converters.parseLong(map.get("UserModeTime"), -1);
            dto.virtualSize = Converters.parseLong(map.get("VirtualSize"), -1);
            dto.windowsVersion = map.get("WindowsVersion");
            dto.workingSetSize = Converters.parseLong(map.get("WorkingSetSize"), -1);
            dto.writeOperationCount = Converters.parseInt(map.get("WriteOperationCount"), -1);
            dto.writeTransferCount = Converters.parseInt(map.get("WriteTransferCount"), -1);

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsNicConfigDto> getNicConfigInfo() {
        List<WindowsNicConfigDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("NICCONFIG");

        for (Map<String, String> map : list) {

            WindowsNicConfigDto dto = new WindowsNicConfigDto();

            dto.caption = map.get("Caption");
            dto.databasePath = map.get("DatabasePath");
            dto.defaultIpGateway = map.get("DefaultIPGateway");
            dto.defaultTtl = Converters.parseInt(map.get("DefaultTTL"), -1);
            dto.description = map.get("Description");
            dto.dhcpEnabled = Converters.parseBoolean(map.get("DHCPEnabled"), false);
            dto.dhcpLeaseExpires = map.get("DHCPLeaseExpires");
            dto.dhcpLeaseObtained = map.get("DHCPLeaseObtained");
            dto.dhcpServer = map.get("DHCPServer");
            dto.dnsDomainSuffixSearchOrder = map.get("DNSDomainSuffixSearchOrder");
            dto.dnsEnabledForWinsResolution = Converters.parseBoolean(map.get("DNSEnabledForWINSResolution"), false);
            dto.dnsHostName = map.get("DNSHostName");
            dto.dnsServerSearchOrder = map.get("DNSServerSearchOrder");
            dto.domainDnsRegistrationEnabled = Converters.parseBoolean(map.get("DomainDNSRegistrationEnabled"), false);
            dto.fullDnsRegistrationEnabled = Converters.parseBoolean(map.get("FullDNSRegistrationEnabled"), false);
            dto.gatewayCostMetric = map.get("GatewayCostMetric");
            dto.index = Converters.parseInt(map.get("Index"), -1);
            dto.interfaceIndex = Converters.parseInt(map.get("InterfaceIndex"), -1);
            dto.ipAddress = map.get("IPAddress");
            dto.ipConnectionMetric = Converters.parseInt(map.get("IPConnectionMetric"), -1);
            dto.ipEnabled = Converters.parseBoolean(map.get("IPEnabled"), false);
            dto.ipFilterSecurityEnabled = Converters.parseBoolean(map.get("IPFilterSecurityEnabled"), false);
            dto.ipSecPermitIpProtocols = map.get("IPSecPermitIPProtocols");
            dto.ipSecPermitTcpPorts = map.get("IPSecPermitTCPPorts");
            dto.ipSecPermitUdpPorts = map.get("IPSecPermitUDPPorts");
            dto.ipSubnet = map.get("IPSubnet");
            dto.macAddress = map.get("MACAddress");
            dto.pmtubhDetectEnabled = Converters.parseBoolean(map.get("PMTUBHDetectEnabled"), false);
            dto.pmtuDiscoveryEnabled = Converters.parseBoolean(map.get("PMTUDiscoveryEnabled"), false);
            dto.serviceName = map.get("ServiceName");
            dto.settingId = map.get("SettingID");
            dto.tcpipNetbiosOptions = map.get("TcpipNetbiosOptions");
            dto.winsEnableLmHostsLookup = Converters.parseBoolean(map.get("WINSEnableLMHostsLookup"), false);
            dto.winsPrimaryServer = map.get("WINSPrimaryServer");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsNicDto> getNicInfo() {
        List<WindowsNicDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("NIC");

        for (Map<String, String> map : list) {

            WindowsNicDto dto = new WindowsNicDto();

            dto.adapterType = map.get("AdapterType");
            dto.adapterTypeId = map.get("AdapterTypeId");
            dto.availability = map.get("Availability");
            dto.caption = map.get("Caption");
            dto.configManagerErrorCode = map.get("ConfigManagerErrorCode");
            dto.configManagerUserConfig = Converters.parseBoolean(map.get("ConfigManagerUserConfig"), false);
            dto.creationClassName = map.get("CreationClassName");
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.guid = map.get("GUID");
            dto.index = Converters.parseInt(map.get("Index"), -1);
            dto.installed = Converters.parseBoolean(map.get("Installed"), false);
            dto.interfaceIndex = Converters.parseInt(map.get("InterfaceIndex"), -1);
            dto.macAddress = map.get("MACAddress");
            dto.manufacturer = map.get("Manufacturer");
            dto.maxNumberControlled = Converters.parseInt(map.get("MaxNumberControlled"), -1);
            dto.name = map.get("Name");
            dto.netConnectionId = map.get("NetConnectionID");
            dto.netConnectionStatus = map.get("NetConnectionStatus");
            dto.netEnabled = Converters.parseBoolean(map.get("NetEnabled"), false);
            dto.physicalAdapter = Converters.parseBoolean(map.get("PhysicalAdapter"), false);
            dto.pnpDeviceId = map.get("PNPDeviceID");
            dto.powerManagementSupported = Converters.parseBoolean(map.get("PowerManagementSupported"), false);
            dto.productName = map.get("ProductName");
            dto.serviceName = map.get("ServiceName");
            dto.speed = Converters.parseLong(map.get("Speed"), -1);
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");
            dto.timeOfLastReset = map.get("TimeOfLastReset");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsMemoryChipDto> getMemoryChipInfo() {
        List<WindowsMemoryChipDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("MEMORYCHIP");

        for (Map<String, String> map : list) {

            WindowsMemoryChipDto dto = new WindowsMemoryChipDto();

            dto.attributes = map.get("Attributes");
            dto.bankLabel = map.get("BankLabel");
            dto.capacity = Converters.parseLong(map.get("Capacity"), -1);
            dto.caption = map.get("Caption");
            dto.configuredClockSpeed = Converters.parseInt(map.get("ConfiguredClockSpeed"), -1);
            dto.configuredVoltage = Converters.parseInt(map.get("ConfiguredVoltage"), -1);
            dto.creationClassName = map.get("CreationClassName");
            dto.dataWidth = Converters.parseInt(map.get("DataWidth"), -1);
            dto.description = map.get("Description");
            dto.deviceLocator = map.get("DeviceLocator");
            dto.formFactor = Converters.parseInt(map.get("FormFactor"), -1);
            dto.interleaveDataDepth = Converters.parseInt(map.get("InterleaveDataDepth"), -1);
            dto.interleavePosition = Converters.parseInt(map.get("InterleavePosition"), -1);
            dto.manufacturer = map.get("Manufacturer");
            dto.maxVoltage = Converters.parseInt(map.get("MaxVoltage"), -1);
            dto.memoryType = map.get("MemoryType");
            dto.minVoltage = Converters.parseInt(map.get("MinVoltage"), -1);
            dto.name = map.get("Name");
            dto.partNumber = map.get("PartNumber");
            dto.serialNumber = map.get("SerialNumber");
            dto.smbiosMemoryType = map.get("SMBIOSMemoryType");
            dto.speed = Converters.parseInt(map.get("Speed"), -1);
            dto.tag = map.get("Tag");
            dto.totalWidth = Converters.parseInt(map.get("TotalWidth"), -1);
            dto.typeDetail = map.get("TypeDetail");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsMemCacheDto> getMemCacheInfo() {
        List<WindowsMemCacheDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("MEMCACHE");

        for (Map<String, String> map : list) {

            WindowsMemCacheDto dto = new WindowsMemCacheDto();

            dto.associativity = map.get("Associativity");
            dto.availability = map.get("Availability");
            dto.blockSize = Converters.parseInt(map.get("BlockSize"), -1);
            dto.cacheType = map.get("CacheType");
            dto.caption = map.get("Caption");
            dto.creationClassName = map.get("CreationClassName");
            dto.currentSram = map.get("CurrentSRAM");
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.errorCorrectType = map.get("ErrorCorrectType");
            dto.installedSize = Converters.parseInt(map.get("InstalledSize"), -1);
            dto.level = Converters.parseInt(map.get("Level"), -1);
            dto.location = map.get("Location");
            dto.maxCacheSize = Converters.parseInt(map.get("MaxCacheSize"), -1);
            dto.name = map.get("Name");
            dto.numberOfBlocks = Converters.parseInt(map.get("NumberOfBlocks"), -1);
            dto.purpose = map.get("Purpose");
            dto.status = map.get("Status");
            dto.statusInfo = map.get("StatusInfo");
            dto.supportedSram = map.get("SupportedSRAM");
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");
            dto.writePolicy = map.get("WritePolicy");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsLogicalDiskDto> getLogicalDiskInfo() {
        List<WindowsLogicalDiskDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("LOGICALDISK");

        for (Map<String, String> map : list) {

            WindowsLogicalDiskDto dto = new WindowsLogicalDiskDto();

            dto.access = map.get("Access");
            dto.caption = map.get("Caption");
            dto.compressed = Converters.parseBoolean(map.get("Compressed"), false);
            dto.creationClassName = map.get("CreationClassName");
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.driveType = map.get("DriveType");
            dto.fileSystem = map.get("FileSystem");
            dto.freeSpace = Converters.parseLong(map.get("FreeSpace"), -1);
            dto.maximumComponentLength = Converters.parseInt(map.get("MaximumComponentLength"), -1);
            dto.mediaType = map.get("MediaType");
            dto.name = map.get("Name");
            dto.quotasDisabled = Converters.parseBoolean(map.get("QuotasDisabled"), false);
            dto.quotasIncomplete = Converters.parseBoolean(map.get("QuotasIncomplete"), false);
            dto.quotasRebuilding = Converters.parseBoolean(map.get("QuotasRebuilding"), false);
            dto.size = Converters.parseLong(map.get("Size"), -1);
            dto.supportsDiskQuotas = Converters.parseBoolean(map.get("SupportsDiskQuotas"), false);
            dto.supportsFileBasedCompression = Converters.parseBoolean(map.get("SupportsFileBasedCompression"), false);
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");
            dto.volumeDirty = Converters.parseBoolean(map.get("VolumeDirty"), false);
            dto.volumeName = map.get("VolumeName");
            dto.volumeSerialNumber = map.get("VolumeSerialNumber");

            ret.add(dto);
        }

        return ret;
    }

    public static List<WindowsDiskDriveDto> getDiskDriveInfo() {
        List<WindowsDiskDriveDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("DISKDRIVE");

        for (Map<String, String> map : list) {

            WindowsDiskDriveDto dto = new WindowsDiskDriveDto();

            dto.bytesPerSector = Converters.parseInt(map.get("BytesPerSector"), -1);
            dto.capabilities = map.get("Capabilities");
            dto.caption = map.get("Caption");
            dto.configManagerErrorCode = map.get("ConfigManagerErrorCode");
            dto.configManagerUserConfig = Converters.parseBoolean(map.get("ConfigManagerUserConfig"), false);
            dto.creationClassName = map.get("CreationClassName");
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.firmwareRevision = map.get("FirmwareRevision");
            dto.index = Converters.parseInt(map.get("Index"), -1);
            dto.interfaceType = map.get("InterfaceType");
            dto.manufacturer = map.get("Manufacturer");
            dto.mediaLoaded = Converters.parseBoolean(map.get("MediaLoaded"), false);
            dto.mediaType = map.get("MediaType");
            dto.model = map.get("Model");
            dto.name = map.get("Name");
            dto.partitions = Converters.parseInt(map.get("Partitions"), -1);
            dto.pnpDeviceId = map.get("PNPDeviceID");
            dto.scsiBus = Converters.parseInt(map.get("SCSIBus"), -1);
            dto.scsiLogicalUnit = Converters.parseInt(map.get("SCSILogicalUnit"), -1);
            dto.scsiPort = Converters.parseInt(map.get("SCSIPort"), -1);
            dto.scsiTargetId = Converters.parseInt(map.get("SCSITargetId"), -1);
            dto.sectorsPerTrack = Converters.parseInt(map.get("SectorsPerTrack"), -1);
            dto.serialNumber = map.get("SerialNumber");
            dto.signature = map.get("Signature");
            dto.size = Converters.parseLong(map.get("Size"), -1);
            dto.status = map.get("Status");
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");
            dto.totalCylinders = Converters.parseInt(map.get("TotalCylinders"), -1);
            dto.totalHeads = Converters.parseInt(map.get("TotalHeads"), -1);
            dto.totalSectors = Converters.parseInt(map.get("TotalSectors"), -1);
            dto.totalTracks = Converters.parseInt(map.get("TotalTracks"), -1);
            dto.tracksPerCylinder = Converters.parseInt(map.get("TracksPerCylinder"), -1);

            ret.add(dto);
        }

        return ret;
    }

    public static WindowsOsDto getOsInfo() {
        if (!OsUtil.isWindows()) {
            return null;
        }
        List<Map<String, String>> list = getWmicList("OS");
        if (list.isEmpty()) {
            return null;
        }

        Map<String, String> map = list.get(0);

        WindowsOsDto dto = new WindowsOsDto();

        dto.bootDevice = map.get("BootDevice");
        dto.buildNumber = map.get("BuildNumber");
        dto.buildType = map.get("BuildType");
        dto.caption = map.get("Caption");
        dto.codeSet = map.get("CodeSet");
        dto.countryCode = map.get("CountryCode");
        dto.creationClassName = map.get("CreationClassName");
        dto.csCreationClassName = map.get("CSCreationClassName");
        dto.csdVersion = map.get("CSDVersion");
        dto.csName = map.get("CSName");
        dto.currentTimeZone = map.get("CurrentTimeZone");
        dto.dataExecutionPrevention32BitApplications = Converters.parseBoolean(map.get("DataExecutionPrevention_32BitApplications"), false);
        dto.dataExecutionPreventionAvailable = Converters.parseBoolean(map.get("DataExecutionPrevention_Available"), false);
        dto.dataExecutionPreventionDrivers = Converters.parseBoolean(map.get("DataExecutionPrevention_Drivers"), false);
        dto.dataExecutionPreventionSupportPolicy = map.get("DataExecutionPrevention_SupportPolicy");
        dto.debug = Converters.parseBoolean(map.get("Debug"), false);
        dto.description = map.get("Description");
        dto.distributed = Converters.parseBoolean(map.get("Distributed"), false);
        dto.encryptionLevel = Converters.parseInt(map.get("EncryptionLevel"), -1);
        dto.foregroundApplicationBoost = Converters.parseInt(map.get("ForegroundApplicationBoost"), -1);
        dto.freePhysicalMemory = Converters.parseLong(map.get("FreePhysicalMemory"), -1);
        dto.freeSpaceInPagingFiles = Converters.parseLong(map.get("FreeSpaceInPagingFiles"), -1);
        dto.freeVirtualMemory = Converters.parseLong(map.get("FreeVirtualMemory"), -1);
        dto.installDate = map.get("InstallDate");
        dto.largeSystemCache = map.get("LargeSystemCache");
        dto.lastBootUpTime = map.get("LastBootUpTime");
        dto.localDateTime = map.get("LocalDateTime");
        dto.locale = map.get("Locale");
        dto.manufacturer = map.get("Manufacturer");
        dto.maxNumberOfProcesses = Converters.parseInt(map.get("MaxNumberOfProcesses"), -1);
        dto.maxProcessMemorySize = Converters.parseLong(map.get("MaxProcessMemorySize"), -1);
        dto.muiLanguages = map.get("MUILanguages");
        dto.name = map.get("Name");
        dto.numberOfLicensedUsers = Converters.parseInt(map.get("NumberOfLicensedUsers"), -1);
        dto.numberOfProcesses = Converters.parseInt(map.get("NumberOfProcesses"), -1);
        dto.numberOfUsers = Converters.parseInt(map.get("NumberOfUsers"), -1);
        dto.operatingSystemSku = map.get("OperatingSystemSKU");
        dto.organization = map.get("Organization");
        dto.osArchitecture = map.get("OSArchitecture");
        dto.osLanguage = map.get("OSLanguage");
        dto.osProductSuite = map.get("OSProductSuite");
        dto.osType = map.get("OSType");
        dto.otherTypeDescription = map.get("OtherTypeDescription");
        dto.paeEnabled = map.get("PAEEnabled");
        dto.plusProductId = map.get("PlusProductID");
        dto.plusVersionNumber = map.get("PlusVersionNumber");
        dto.portableOperatingSystem = Converters.parseBoolean(map.get("PortableOperatingSystem"), false);
        dto.primary = Converters.parseBoolean(map.get("Primary"), false);
        dto.productType = map.get("ProductType");
        dto.registeredUser = map.get("RegisteredUser");
        dto.serialNumber = map.get("SerialNumber");
        dto.servicePackMajorVersion = map.get("ServicePackMajorVersion");
        dto.servicePackMinorVersion = map.get("ServicePackMinorVersion");
        dto.sizeStoredInPagingFiles = map.get("SizeStoredInPagingFiles");
        dto.status = map.get("Status");
        dto.suiteMask = map.get("SuiteMask");
        dto.systemDevice = map.get("SystemDevice");
        dto.systemDirectory = map.get("SystemDirectory");
        dto.systemDrive = map.get("SystemDrive");
        dto.totalSwapSpaceSize = Converters.parseLong(map.get("TotalSwapSpaceSize"), -1);
        dto.totalVirtualMemorySize = Converters.parseLong(map.get("TotalVirtualMemorySize"), -1);
        dto.totalVisibleMemorySize = Converters.parseLong(map.get("TotalVisibleMemorySize"), -1);
        dto.version = map.get("Version");
        dto.windowsDirectory = map.get("WindowsDirectory");
        return dto;
    }


    public static List<WindowsCpuDto> getCpuInfo() {
        List<WindowsCpuDto> ret = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return ret;
        }

        List<Map<String, String>> list = getWmicList("CPU");

        for (Map<String, String> map : list) {

            WindowsCpuDto dto = new WindowsCpuDto();

            dto.addressWidth = Converters.parseInt(map.get("AddressWidth"), -1);
            dto.architecture = map.get("Architecture");
            dto.assetTag = map.get("AssetTag");
            dto.availability = map.get("Availability");
            ;
            dto.caption = map.get("Caption");
            dto.characteristics = map.get("Characteristics");
            dto.configManagerErrorCode = map.get("ConfigManagerErrorCode");
            dto.configManagerUserConfig = map.get("ConfigManagerUserConfig");
            dto.cpuStatus = map.get("CpuStatus");
            dto.creationClassName = map.get("CreationClassName");
            dto.currentClockSpeed = Converters.parseInt(map.get("CurrentClockSpeed"), -1);
            dto.currentVoltage = Converters.parseInt(map.get("CurrentVoltage"), -1);
            dto.dataWidth = Converters.parseInt(map.get("DataWidth"), -1);
            dto.description = map.get("Description");
            dto.deviceId = map.get("DeviceID");
            dto.errorCleared = map.get("ErrorCleared");
            dto.errorDescription = map.get("ErrorDescription");
            dto.extClock = Converters.parseInt(map.get("ExtClock"), -1);
            dto.family = map.get("Family");
            dto.installDate = map.get("InstallDate");
            dto.l2CacheSize = Converters.parseInt(map.get("L2CacheSize"), -1);
            dto.l2CacheSpeed = Converters.parseInt(map.get("L2CacheSpeed"), -1);
            dto.l3CacheSize = Converters.parseInt(map.get("L3CacheSize"), -1);
            dto.l3CacheSpeed = Converters.parseInt(map.get("L3CacheSpeed"), -1);
            dto.lastErrorCode = map.get("LastErrorCode");
            dto.level = Converters.parseInt(map.get("Level"), -1);
            dto.loadPercentage = Converters.parseInt(map.get("LoadPercentage"), -1);
            dto.manufacturer = map.get("Manufacturer");
            dto.maxClockSpeed = Converters.parseInt(map.get("MaxClockSpeed"), -1);
            dto.name = map.get("Name");
            dto.numberOfCores = Converters.parseInt(map.get("NumberOfCores"), -1);
            dto.numberOfEnabledCore = Converters.parseInt(map.get("NumberOfEnabledCore"), -1);
            dto.numberOfLogicalProcessors = Converters.parseInt(map.get("NumberOfLogicalProcessors"), -1);
            dto.otherFamilyDescription = map.get("OtherFamilyDescription");
            dto.partNumber = map.get("PartNumber");
            dto.pnpDeviceId = map.get("PNPDeviceID");
            dto.powerManagementCapabilities = map.get("PowerManagementCapabilities");
            dto.powerManagementSupported = Converters.parseBoolean(map.get("PowerManagementSupported"), false);
            dto.processorId = map.get("ProcessorId");
            dto.processorType = map.get("ProcessorType");
            dto.revision = map.get("Revision");
            dto.role = map.get("Role");
            dto.secondLevelAddressTranslationExtensions = Converters.parseBoolean(map.get("SecondLevelAddressTranslationExtensions"), false);
            dto.serialNumber = map.get("SerialNumber");
            dto.socketDesignation = map.get("SocketDesignation");
            dto.status = map.get("Status");
            dto.statusInfo = map.get("StatusInfo");
            dto.stepping = map.get("Stepping");
            dto.systemCreationClassName = map.get("SystemCreationClassName");
            dto.systemName = map.get("SystemName");
            dto.threadCount = Converters.parseInt(map.get("ThreadCount"), -1);
            dto.uniqueId = map.get("UniqueId");
            dto.upgradeMethod = map.get("UpgradeMethod");
            dto.version = map.get("Version");
            dto.virtualizationFirmwareEnabled = Converters.parseBoolean(map.get("VirtualizationFirmwareEnabled"), false);
            dto.vmMonitorModeExtensions = Converters.parseBoolean(map.get("VMMonitorModeExtensions"), false);
            dto.voltageCaps = map.get("VoltageCaps");

            ret.add(dto);
        }

        return ret;
    }


    public static List<Map<String, String>> getWmicList(String type) {
        List<Map<String, String>> list = new ArrayList<>();
        if (!OsUtil.isWindows()) {
            return list;
        }
        String rs = OsUtil.runCmd("wmic " + type + " get *").trim();
        String[] lines = rs.split("\n");
        if (lines.length > 1) {
            String title = lines[0];
            String[] columns = title.split("\\s+");
            for (String column : columns) {
                if ("".equals(column)) {
                    continue;
                }
                String out = OsUtil.runCmd("wmic " + type + " get " + column).trim();
                if ("".equals(out)) {
                    continue;
                }
                String[] arr = out.split("\n");
                if (arr.length < 2) {
                    continue;
                }
                if (arr[0].trim().equalsIgnoreCase(column)) {
                    for (int i = 1; i < arr.length; i++) {
                        String value = arr[i].trim();
                        if (list.size() < i) {
                            list.add(new LinkedHashMap<>());
                        }
                        list.get(i - 1).put(column, value);
                    }
                }
            }
        }
        return list;
    }

}
