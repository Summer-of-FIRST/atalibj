package ata2014.subsystems;

import ata2014.main.Ports;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public interface BackLoader extends Ports {

    DualActionSolenoidModule backLoaderPiston = new DualActionSolenoidModule(BACK_LOADER_IN, BACK_LOADER_OUT);

    Subsystem backLoader = new Subsystem(new Module[]{
        backLoaderPiston
    });
}