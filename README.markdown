Haqua - A collection of hacks to work around issues in the Aqua look and feel

Copyright (C) 2014  Trejkaz, Haqua Project


USAGE
-----

Usage is the same as any other custom look and feel:

    // Guard against using this look and feel unless you're on a platform
    // where Aqua is the system look and feel (i.e. Mac OS X.)
    if ("com.apple.laf.AquaLookAndFeel".equals(UIManager.getSystemLookAndFeelClassName())) {
        try {
            UIManager.setLookAndFeel("org.trypticon.haqua.HaquaLookAndFeel");
        } catch (Exception e) {
            // Fall back in some sensible fashion.
        }
    }


BUILDING
--------

You'll need a Java build environment.  I'm developing this on Java 7 at
the moment.  You'll also need to be on Mac OS X, since the JRE doesn't include
the com.apple classes on other platforms.

You'll also need Ant.  All the other dependencies should be bundled.
If something is missing, prod me to fix it.

To build, execute 'ant' in the top directory. Files for the distribution
will appear in build/dist.


