/***
 * Excerpted from "Learn to Program with Minecraft Plugins, CanaryMod Edition",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/ahmine2 for more book information.
***/
package cleararea;

import net.canarymod.plugin.Plugin;
import net.canarymod.logger.Logman;
import net.canarymod.Canary;
import net.canarymod.commandsys.*;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;

//
// This is a bit convoluted, but it will let you call the "buildMyHouse"
// function without having to see or read this code.
// You might want to pull out the "makeCube" function to use
// in another plugin; it's a triple-nested for-loop
// to make a 3D block.
//

public class ClearArea extends Plugin implements CommandListener {

  public static Logman logger;
  public static Location origin = null;
  public static boolean firstHouse = true;
  
  // Create a 3D cube, offset from the saved "origin"
  private static void makeCube(int offsetX, int offsetY, int offsetZ,
                            int width, int height, BlockType what) {
    int i, j, k;
    Location loc = new Location(origin.getWorld(), 0,0,0, 0,0);

    // Base is i X j, k goes up for height
    for (i=0; i< width; i++) {
      for (j=0; j<width; j++) {
        for (k=0; k< height; k++) {
          loc.setX(origin.getX() + offsetX + i);
          loc.setZ(origin.getZ() + offsetZ + j);
          loc.setY(origin.getY() + offsetY + k);
          origin.getWorld().setBlockAt(loc, what);
        }
      }
    }
  }    
  public ClearArea() {
    logger = getLogman();
  }
  
  @Override
  public boolean enable() {
    logger.info("Starting up ClearArea");
    
    try {
      Canary.commands().registerCommands(this, this, false);
    } catch (CommandDependencyException e) {
      logger.error("Duplicate command name");
    }
    return true;
  }
  
  @Override
  public void disable() {
    logger.info("Stopping");
  }
    
  // Called by reader's code
  // Minimum of 5 X 5
  public static void buildMyHouse(int width, int height) {
          
    // Floor the dimensions at no less than 5 X 5 
    if (width < 5) {
      width = 5;
    }
    if (height < 5) {
      height = 5;
    }
    
    if (firstHouse) {
      // Center the first house on the player
      origin.setY(origin.getY() - 1);
      origin.setZ(origin.getZ() - (width/2));  
      origin.setX(origin.getX() - (width/2));
      firstHouse = false;
    }
    
    // Set the whole area to wood
    makeCube(0,1,0,width, height, BlockType.Air);
    // Set the inside of the cube to air
    // makeCube(1,1,1,width-2, height-2, BlockType.Air);

  }

  @Command(aliases = { "cleararea" },
            description = "Clear an Area",
            permissions = { "" },
            toolTip = "/cleararea")
  public void buildAHouseCommand(MessageReceiver caller, String[] parameters) {
    if (caller instanceof Player) { 
      Player me = (Player)caller;
      origin = me.getLocation();
      firstHouse = true;
      AreaSize.build_me();
    }
  }
}
