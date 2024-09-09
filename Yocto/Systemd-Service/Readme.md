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

[Install]
WantedBy=multi-user.target
```

