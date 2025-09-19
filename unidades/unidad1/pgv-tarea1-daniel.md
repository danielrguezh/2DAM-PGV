# Ejecución y comprensión de procesos en linux.
## Bloque 1: Conceptos básicos (teoría)

### 1. Define qué es un **proceso** y en qué se diferencia de un **programa**.  
Un proceso es un programa en ejecucion, q a su vez es un conjunto de intrucciones a realizar por un dispositivo, por lo que un proceso es una entidad activa y un proceso una entidad pasiva.

<details>
    <summary>Fuentes:</summary>
    https://github.com/jpexposito/code-learn/blob/main/segundo/pgv/1-concurrente/README.md
</details>

### 2. Explica qué es el **kernel** y su papel en la gestión de procesos.  
El kernel es el núcleo de los sistemas operativos y son los gestores de los programas de una máquina y de la conexión de estos con el hardware.
<details>
    <summary>Fuentes:</summary>
    https://es.wikipedia.org/wiki/Núcleo_(informática)
</details>

### 3. ¿Qué son **PID** y **PPID**? Explica con un ejemplo.
PID es la ID de un proceso mientras PPID es la ID del proceso que lo inició. Por ejemplo, si abres una aplicación desde el terminal recibirás el PID del terminal como PPID del proceso que inicío la aplicación.
<details>
    <summary>Fuentes:</summary>
    https://www.tumblr.com/sololinuxes/656041622503604224/que-es-ppid-y-como-identificarlo
</details>

### 4. Describe qué es un **cambio de contexto** y por qué es costoso.
Es el proceso que realiza un sistema operativo para detener un proceso o hilo de ejecución y cargar el estado de otro para que continúe desde donde fue interrumpido. Es costoso porque sobrecarga el sistema, se pierde rendimiento e impacta en la concurrencia.

<details>
    <summary>Fuentes:</summary>
    https://clickup.com/es-ES/blog/56983/cambio-de-contexto
</details>

### 5. Explica qué es un **PCB (Process Control Block)** y qué información almacena.  
Un PCB (Bloque de Control de Procesos) es una estructura de datos utilizada por el sistema operativo para almacenar y gestionar información crucial sobre cada proceso en ejecución, siendo esta el nombre e ID del proceso.
<details>
    <summary>Fuentes:</summary>
    https://es.linkedin.com/advice/0/what-some-common-pcb-attributes-how-do-affect?lang=es#:~:text=Bloques%20de%20control%20de%20procesos,mecanismos%20de%20comunicación%20y%20sincronización.
</details>

### 6. Diferencia entre **proceso padre** y **proceso hijo**.  
Un proceso padre es el que inicia otro proceso hijo adyacente al primero.
<details>
    <summary>Fuentes:</summary>
    https://www.tumblr.com/sololinuxes/656041622503604224/que-es-ppid-y-como-identificarlo
</details>

### 7. Explica qué ocurre cuando un proceso queda **huérfano** en Linux.  
Si un proceso padre finaliza antes que su proceso hijo sin su padre original, el proceso en ejecución se conoce como proceso huerfano. En Linux, los procesos huérfanos son adoptados automáticamente por el proceso init o systemd (normalmente PID 1), lo que les permite continuar ejecutándose en segundo plano.
<details>
    <summary>Fuentes:</summary>
    https://www-greptile-com.translate.goog/bug-wiki/memory-resource-management/orphaned-process?_x_tr_sl=en&_x_tr_tl=es&_x_tr_hl=es&_x_tr_pto=rq&_x_tr_hist=true
</details>


### 8. ¿Qué es un proceso **zombie**? Da un ejemplo de cómo puede ocurrir.  
Un proceso zombie es un proceso informático que ha terminado su ejecución pero permanece en la tabla de procesos del sistema, consumiendo un PID (identificador de proceso) y una entrada, pero sin asignar memoria RAM o CPU. Ocurre cuando un proceso padre no recupera el estado de salida de un proceso hijo que ya finalizó, usualmente por no llamar a la función wait() o waitpid(). Por ejemplo:
```
#include <stdlib.h>
#include <stdio.h>
int main() {
        pid_t pid;
        int i=0;
        while (1)  {
                pid = fork();
                if (pid > 0) {
                        // Proceso padre
                        printf("Padre %d\n",i + 1);
                        sleep(1);
                } else {
                        // Proceso hijo
                        printf("Zombie %d\n",i + 1);
                        exit(0);
                }
        i++;
        }
        return 0;
}
```
<details>
    <summary>Fuentes:</summary>
    https://bytelearning.blogspot.com/2016/11/procesos-zombie-en-linux-que-son-y-como.html
</details>

### 9. Diferencia entre **concurrencia** y **paralelismo**.
La concurrencia es la capacidad de un sistema para gestionar múltiples tareas que progresan de forma independiente, mientras que el paralelismo es la capacidad de ejecutar varias tareas simultáneamente.
<details>
    <summary>Fuentes:</summary>
    https://blog.thedojo.mx/2019/04/17/la-diferencia-entre-concurrencia-y-paralelismo.html#:~:text=Como%20resumen:,charla:%20Concurrency%20is%20not%20Parallelism.
</details>

### 10. Explica qué es un **hilo (thread)** y en qué se diferencia de un proceso.  
Un hilo no es más que cada una de esas líneas de flujo que puede tener un proceso ejecutándose de forma concurrente. Así, un proceso estará formado por, al menos, un hilo de ejecución, el hilo principal. Si el proceso tiene varios hilos, cada uno es una unidad de ejecución ligera.
<details>
    <summary>Fuentes:</summary>
    https://github.com/jpexposito/code-learn/blob/main/segundo/pgv/1-concurrente/README.md
</details>

## Bloque 2: Práctica con comandos en Linux

### 11. Usa echo $$ para mostrar el PID del proceso actual.
```bash
echo $$ 
4934
```
### 12. Usa echo $PPID para mostrar el PID del proceso padre.
```bash
echo $PPID
4925
```
### 13. Ejecuta pidof systemd y explica el resultado.
```bash
pidof systemd 
3316
```
systemd se ejecuta con el PID 1 como instancia del sistema y selecciona otra instancia de usuario para cada sesión iniciada.
### 14. Abre un programa gráfico (ejemplo: gedit) y usa pidof para obtener sus PID.
```bash
gedit &
[1] 4321

pidof gedit
4321 4325
```
### 15. Ejecuta ps -e y explica qué significan sus columnas.
```bash
ps -e 
    PID TTY          TIME CMD
      1 ?        00:00:01 systemd
      2 ?        00:00:00 kthreadd
      3 ?        00:00:00 pool_workqueue_release
      4 ?        00:00:00 kworker/R-rcu_g
      5 ?        00:00:00 kworker/R-rcu_p
      6 ?        00:00:00 kworker/R-slub_
      7 ?        00:00:00 kworker/R-netns
      8 ?        00:00:00 kworker/0:0-rcu_par_gp
     10 ?        00:00:00 kworker/0:0H-events_highpri
     12 ?        00:00:00 kworker/R-mm_pe
     13 ?        00:00:00 rcu_tasks_kthread
     14 ?        00:00:00 rcu_tasks_rude_kthread
     15 ?        00:00:00 rcu_tasks_trace_kthread
     16 ?        00:00:00 ksoftirqd/0
     17 ?        00:00:00 rcu_preempt
     18 ?        00:00:00 migration/0
     19 ?        00:00:00 idle_inject/0
     20 ?        00:00:00 cpuhp/0
     etc...
```
* PID corresponde a la ID del proceso. 
* TTY indica el terminal asociado al proceso('?' significa que no está asociado a ningún terminal interactivo). 
* TIME es el tiempo de CPU acumulado que el proceso ha consumido desde que inició.
* CMD es el nombre del comando o programa que lanzó el proceso.

### 16. Ejecuta ps -f y observa la relación entre procesos padre e hijo.
```bash
ps -f
UID          PID    PPID  C STIME TTY          TIME CMD
dam         4934    4925  0 14:39 pts/0    00:00:00 bash
dam        17408    4934  0 15:03 pts/0    00:00:00 ps -f
```
### 17. Usa ps -axf o pstree para mostrar el árbol de procesos y dibújalo.
```bash
 pstree
systemd─┬─ModemManager───3*[{ModemManager}]
        ├─NetworkManager───3*[{NetworkManager}]
        ├─accounts-daemon───3*[{accounts-daemon}]
        ├─agetty
        ├─apache2───5*[apache2]
        ├─at-spi2-registr───3*[{at-spi2-registr}]
        ├─avahi-daemon───avahi-daemon
        ├─blkmapd
        ├─colord───3*[{colord}]
        ├─containerd───16*[{containerd}]
        ├─containerd-shim─┬─apache2───5*[apache2]
        │                 └─10*[{containerd-shim}]
        ├─containerd-shim─┬─apache2───6*[apache2]
        │                 └─11*[{containerd-shim}]
        ├─cron
        ├─csd-printer───3*[{csd-printer}]
        ├─cups-browsed───3*[{cups-browsed}]
        ├─cupsd
        ├─dbus-daemon
        ├─dnsmasq───dnsmasq
        ├─dockerd─┬─4*[docker-proxy───7*[{docker-proxy}]]
        │         └─28*[{dockerd}]
        ├─fsidd
        ├─fwupd───5*[{fwupd}]
        ├─irqbalance───{irqbalance}
        ├─2*[kerneloops]
        ├─lightdm─┬─Xorg───12*[{Xorg}]
        │         ├─lightdm─┬─cinnamon-sessio─┬─agent───3*[{agent}]
        │         │         │                 ├─applet.py
        │         │         │                 ├─at-spi-bus-laun─┬─dbus-daemon
        │         │         │                 │                 └─4*[{at-spi-bus-laun}]
        │         │         │                 ├─blueman-applet───4*[{blueman-applet}]
        │         │         │                 ├─cinnamon-killer───4*[{cinnamon-killer}]
        │         │         │                 ├─cinnamon-launch─┬─cinnamon─┬─nemo───5*[{nemo}]
        │         │         │                 │                 │          └─26*[{cinnamon}]
        │         │         │                 │                 └─6*[{cinnamon-launch}]
        │         │         │                 ├─csd-a11y-settin───4*[{csd-a11y-settin}]
        │         │         │                 ├─csd-automount───4*[{csd-automount}]
        │         │         │                 ├─csd-background───4*[{csd-background}]
        │         │         │                 ├─csd-clipboard───3*[{csd-clipboard}]
        │         │         │                 ├─csd-color───4*[{csd-color}]
        │         │         │                 ├─csd-housekeepin───4*[{csd-housekeepin}]
        │         │         │                 ├─csd-keyboard───4*[{csd-keyboard}]
        │         │         │                 ├─csd-media-keys───4*[{csd-media-keys}]
        │         │         │                 ├─csd-power───4*[{csd-power}]
        │         │         │                 ├─csd-print-notif───3*[{csd-print-notif}]
        │         │         │                 ├─csd-screensaver───3*[{csd-screensaver}]
        │         │         │                 ├─csd-settings-re───4*[{csd-settings-re}]
        │         │         │                 ├─csd-wacom───3*[{csd-wacom}]
        │         │         │                 ├─csd-xsettings───4*[{csd-xsettings}]
        │         │         │                 ├─evolution-alarm───7*[{evolution-alarm}]
        │         │         │                 ├─nemo-desktop─┬─firefox-bin─┬─3*[Isolated Web Co───26*[{Isolated Web Co}]]
        │         │         │                 │              │             ├─2*[Isolated Web Co───28*[{Isolated Web Co}]]
        │         │         │                 │              │             ├─2*[Isolated Web Co───27*[{Isolated Web Co}]]
        │         │         │                 │              │             ├─Privileged Cont───27*[{Privileged Cont}]
        │         │         │                 │              │             ├─RDD Process───4*[{RDD Process}]
        │         │         │                 │              │             ├─Socket Process───5*[{Socket Process}]
        │         │         │                 │              │             ├─Utility Process───4*[{Utility Process}]
        │         │         │                 │              │             ├─3*[Web Content───17*[{Web Content}]]
        │         │         │                 │              │             ├─WebExtensions───26*[{WebExtensions}]
        │         │         │                 │              │             ├─crashhelper───{crashhelper}
        │         │         │                 │              │             └─115*[{firefox-bin}]
        │         │         │                 │              └─5*[{nemo-desktop}]
        │         │         │                 ├─socat
        │         │         │                 ├─xapp-sn-watcher───4*[{xapp-sn-watcher}]
        │         │         │                 └─4*[{cinnamon-sessio}]
        │         │         └─3*[{lightdm}]
        │         └─3*[{lightdm}]
        ├─mintUpdate───8*[{mintUpdate}]
        ├─mintreport-tray───4*[{mintreport-tray}]
        ├─nfsdcld
        ├─polkitd───3*[{polkitd}]
        ├─power-profiles-───3*[{power-profiles-}]
        ├─prometheus-node───6*[{prometheus-node}]
        ├─rpc.idmapd
        ├─rpc.mountd
        ├─rpc.statd
        ├─rpcbind
        ├─rsyslogd───3*[{rsyslogd}]
        ├─rtkit-daemon───2*[{rtkit-daemon}]
        ├─smartd
        ├─socat
        ├─switcheroo-cont───3*[{switcheroo-cont}]
        ├─systemd─┬─(sd-pam)
        │         ├─chrome_crashpad───2*[{chrome_crashpad}]
        │         ├─code─┬─code───code───24*[{code}]
        │         │      ├─code───code─┬─code───21*[{code}]
        │         │      │             └─code───17*[{code}]
        │         │      ├─code───14*[{code}]
        │         │      ├─code───15*[{code}]
        │         │      ├─code───17*[{code}]
        │         │      ├─code─┬─bash
        │         │      │      ├─bash───docker─┬─docker-compose───16*[{docker-compose}]
        │         │      │      │               └─10*[{docker}]
        │         │      │      └─17*[{code}]
        │         │      ├─code─┬─5*[code───7*[{code}]]
        │         │      │      └─15*[{code}]
        │         │      └─40*[{code}]
        │         ├─dbus-daemon
        │         ├─dconf-service───3*[{dconf-service}]
        │         ├─evolution-addre───6*[{evolution-addre}]
        │         ├─evolution-calen───9*[{evolution-calen}]
        │         ├─evolution-sourc───4*[{evolution-sourc}]
        │         ├─gnome-keyring-d───4*[{gnome-keyring-d}]
        │         ├─gnome-terminal-─┬─bash───pstree
        │         │                 └─5*[{gnome-terminal-}]
        │         ├─goa-daemon───4*[{goa-daemon}]
        │         ├─goa-identity-se───3*[{goa-identity-se}]
        │         ├─gvfs-afc-volume───4*[{gvfs-afc-volume}]
        │         ├─gvfs-goa-volume───3*[{gvfs-goa-volume}]
        │         ├─gvfs-gphoto2-vo───3*[{gvfs-gphoto2-vo}]
        │         ├─gvfs-mtp-volume───3*[{gvfs-mtp-volume}]
        │         ├─gvfs-udisks2-vo───4*[{gvfs-udisks2-vo}]
        │         ├─gvfsd─┬─gvfsd-trash───4*[{gvfsd-trash}]
        │         │       └─3*[{gvfsd}]
        │         ├─gvfsd-fuse───6*[{gvfsd-fuse}]
        │         ├─gvfsd-metadata───3*[{gvfsd-metadata}]
        │         ├─obexd
        │         ├─2*[pipewire───2*[{pipewire}]]
        │         ├─pipewire-pulse───2*[{pipewire-pulse}]
        │         ├─powerline-daemo
        │         ├─wireplumber───5*[{wireplumber}]
        │         ├─xdg-desktop-por───6*[{xdg-desktop-por}]
        │         ├─2*[xdg-desktop-por───4*[{xdg-desktop-por}]]
        │         ├─xdg-document-po─┬─fusermount3
        │         │                 └─6*[{xdg-document-po}]
        │         └─xdg-permission-───3*[{xdg-permission-}]
        ├─systemd-journal
        ├─systemd-logind
        ├─systemd-machine
        ├─systemd-resolve
        ├─systemd-timesyn───{systemd-timesyn}
        ├─systemd-udevd
        ├─touchegg───3*[{touchegg}]
        ├─udisksd───5*[{udisksd}]
        ├─upowerd───3*[{upowerd}]
        ├─virtlockd
        ├─virtlogd
        ├─winbindd───wb[A108PC01]
        ├─wpa_supplicant
        └─zed───2*[{zed}]

```
### 18. Ejecuta top o htop y localiza el proceso con mayor uso de CPU.

* htop -> SortBy(F6) -> PERCENT_CPU = htop(6.2%) 

### 19. Ejecuta sleep 100 en segundo plano y busca su PID con ps.
```bash
sleep 100 &
[1] 64458

ps aux | grep 'sleep 100'
dam        64458  0.0  0.0  11152  2176 pts/0    S    18:17   0:00 sleep 100
dam        64925  0.0  0.0  11780  2304 pts/0    S+   18:18   0:00 grep --color=auto sleep 100

```
### 20. Finaliza un proceso con kill <PID> y comprueba con ps que ya no está.
```bash
pgrep -f 'bluetooth'
3899
kill 3899
pgrep -f 'bluetooth'
```

## Bloque 3: Procesos y jerarquía
### 21. Identifica el **PID del proceso init/systemd** y explica su función.
```bash
ps -p 1 -o pid,comm
PID COMMAND
  1 systemd
```
Es el primer proceso que se ejecuta tras el kernel, administra arranque, ejecución y apagado del sistema y, gestiona la limpieza de los procesos huerfanos.
### 22. Explica qué ocurre con el **PPID** de un proceso hijo si su padre termina antes.
El proceso hijo adopta el PPID 1 del terminal.
<details>
    <summary>Fuentes:</summary>
    https://w3.ual.es/~jjfdez/SOA/pract3.html#:~:text=El%20PPID%20de%20un%20proceso,y%20su%20PID%20es%201.
</details>

### 23. Ejecuta un programa que genere varios procesos hijos y observa sus PIDs con `ps`.
```bash
firefox & 
[2] 83238

ps aux | grep 'firefox'

dam        82076  0.0  0.2 2436532 77540 ?       Sl   19:00   0:00 /usr/lib/firefox/firefox-bin -contentproc -isForBrowser -prefsHandle 0:41463 -prefMapHandle 1:272639 -jsInitHandle 2:245828 -parentBuildID 20250522210034 -sandboxReporter 3 -chrootClient 4 -ipcHandle 5 -initialChannelId {ff040c03-edd2-4de2-9b3b-1b663268f3de} -parentPid 4143 -crashReporter 6 -crashHelperPid 4148 -greomni /usr/lib/firefox/omni.ja -appomni /usr/lib/firefox/browser/omni.ja -appDir /usr/lib/firefox/browser 39 tab
dam        83238  0.0  0.0  11780  2304 pts/0    S+   19:02   0:00 grep --color=auto firefox

```
### 24. Haz que un proceso quede en **estado suspendido** con `Ctrl+Z` y réanúdalo con `fg`.
```bash
xeyes
^Z
[1]+  Detenido                xeyes

fg
xeyes

```
### 25. Lanza un proceso en **segundo plano** con `&` y obsérvalo con `jobs`.
```bash
sleep 100 &
[2] 100064
jobs
[2]-  Ejecutando              sleep 100 &

```
### 26. Explica la diferencia entre los estados de un proceso: **Running, Sleeping, Zombie, Stopped**.
La diferencia entre los estados de un proceso es: Running es cuando el proceso está activamente usando la CPU; Sleeping o bloqueado significa que el proceso espera un evento, quedando inactivo hasta que este ocurra; Zombie es un proceso que ya terminó de ejecutar pero que su padre aún no ha recogido su estado de salida, por lo que no se elimina completamente; y Stopped se refiere a un proceso que ha sido suspendido temporalmente.
<details>
    <summary>Fuentes:</summary>
    https://www.fing.edu.uy/tecnoinf/mvd/cursos/so/material/teo/so04-procesos.pdf
</details>

### 27. Usa `ps -eo pid,ppid,stat,cmd` para mostrar los estados de varios procesos.
```bash
ps -eo pid,ppid,stat,cmd
    PID    PPID STAT CMD
      1       0 Ss   /sbin/init splash
      2       0 S    [kthreadd]
      3       2 S    [pool_workqueue_release]
      4       2 I<   [kworker/R-rcu_g]
      5       2 I<   [kworker/R-rcu_p]
      6       2 I<   [kworker/R-slub_]
      7       2 I<   [kworker/R-netns]
     10       2 I<   [kworker/0:0H-events_highpri]
     12       2 I<   [kworker/R-mm_pe]
     13       2 I    [rcu_tasks_kthread]
     14       2 I    [rcu_tasks_rude_kthread]
     15       2 I    [rcu_tasks_trace_kthread]
     16       2 S    [ksoftirqd/0]
     17       2 I    [rcu_preempt]
     18       2 S    [migration/0]
     19       2 S    [idle_inject/0]
     20       2 S    [cpuhp/0]
etc...
```
### 28. Ejecuta `watch -n 1 ps -e` y observa cómo cambian los procesos en tiempo real.
```bash
watch -n 1 ps -e
[2]+  Detenido                watch -n 1 ps -e
```
### 29. Explica la diferencia entre ejecutar un proceso con `&` y con `nohup`.
La principal diferencia es que & solo envía el proceso a segundo plano para liberar tu terminal, pero el proceso se detiene si cierras la sesión, mientras que nohup hace que el proceso ignore la señal de hangup (HUP) que se envía al cerrar la terminal, permitiendo que el proceso continúe ejecutándose en segundo plano incluso después de desconectarte.
<details>
    <summary>Fuentes:</summary>
    https://es.scribd.com/document/597607490/Lab3-docx
</details>

### 30. Usa `ulimit -a` para ver los límites de recursos de procesos en tu sistema.
```bash
ulimit -a
real-time non-blocking time  (microseconds, -R) unlimited
core file size              (blocks, -c) 0
data seg size               (kbytes, -d) unlimited
scheduling priority                 (-e) 0
file size                   (blocks, -f) unlimited
pending signals                     (-i) 125404
max locked memory           (kbytes, -l) 4026020
max memory size             (kbytes, -m) unlimited
open files                          (-n) 1024
pipe size                (512 bytes, -p) 8
POSIX message queues         (bytes, -q) 819200
real-time priority                  (-r) 0
stack size                  (kbytes, -s) 8192
cpu time                   (seconds, -t) unlimited
max user processes                  (-u) 125404
virtual memory              (kbytes, -v) unlimited
file locks                          (-x) unlimited

```