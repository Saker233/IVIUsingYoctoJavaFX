## WiFi Service

To connect to WiFi automatically, we created a wpa_supplicant configuration file and a systemd service to manage it.

### wpa_supplicant Configuration File

```
ini
Copy code
ctrl_interface=/var/run/wpa_supplicant
ctrl_interface_group=0
update_config=1

network={
    ssid="ITI_Students"
    psk="iTi_2@24"
    key_mgmt=WPA-PSK
}
```


### Systemd Service

To automate the wpa_supplicant configuration, we created a systemd service with the following configuration:

```
ini
Copy code
[Unit]
Description=WPA Supplicant
Before=network.target

[Service]
ExecStart=/sbin/wpa_supplicant -c /etc/wpa_supplicant.conf -i wlan0
Restart=on-failure


## GPS Service

We use a bash script to parse GPS data from gpspipe, extracting latitude, longitude, and current speed. To automate this process, we created a systemd service.

### Systemd Service

Here’s the configuration for the GPS service:


ini
Copy code
[Unit]
Description=GPS Service
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
ExecStart=/bin/bash /home/root/gps.sh
Restart=on-failure
User=root

[Install]
WantedBy=multi-user.target



## DNS Configuration Service

Since the Yocto image does not come with a pre-configured DNS setup, we created a systemd service to automate DNS configuration on boot.

### Systemd Service

Here’s the configuration for the DNS setup service:


ini
Copy code
[Unit]
Description=Setup DNS Service
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
ExecStart=/home/root/setup_dns.sh
Restart=on-failure
User=root

[Install]
WantedBy=multi-user.target


[Install]
WantedBy=multi-user.target
```

