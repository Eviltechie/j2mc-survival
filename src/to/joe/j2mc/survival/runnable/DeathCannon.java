package to.joe.j2mc.survival.runnable;

import to.joe.j2mc.maps.J2MC_Maps;
import to.joe.j2mc.survival.J2MC_Survival;

public class DeathCannon implements Runnable {

    J2MC_Survival plugin;

    public DeathCannon(J2MC_Survival survival) {
        plugin = survival;
    }

    @Override
    public void run() {
        if (J2MC_Maps.getGameWorld().getTime() >= 12000 && J2MC_Maps.getGameWorld().getTime() < 13000)
            plugin.getGame().announceDead();
    }

}
