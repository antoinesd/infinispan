package org.infinispan.scripting;

import org.infinispan.factories.components.ModuleMetadataFileFinder;

public class ScriptingMetadataFileFinder implements ModuleMetadataFileFinder {
   @Override
   public String getMetadataFilename() {
      return "infinispan-scripting-component-metadata.dat";
   }
}
