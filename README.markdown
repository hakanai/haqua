Haqua - A collection of hacks to work around issues in the Aqua look and feel

Copyright (C) 2014  Trejkaz, Haqua Project


Usage
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


Hacks in use
------------

JComboBox:

* Popup sets its JList background to match the popup itself.

JTable:

* Rows are painted with stripes.
* Row and column spacing is set to 0 to eliminate the seams you would usually see.
* Table paints the background all the way to the bottom of the viewport it's placed inside.

JTree:

* Selection colour paints across the entire row (known issue: animation for the disclosure
  triangle on selected rows does not occur at the moment, because of the way this hack was
  implemented.)
* Mouse clicks on the row select the node (instead of just clicks on the node.)
* Selected nodes paint using the proper inactive alternate colours when the window is inactive.

JPanel:

* Panels are non-opaque by default, to fix appearance when placed inside JTabbedPane.

JScrollPane:

* Scroll panes (and their contained viewports) are non-opaque by default, to fix appearance
  the pane contains a non-opaque component.

TitledBorder:

* Borders appear in the proper Aqua style instead of the default etched border.
* Font size is set to match native apps better.


Building
--------

You'll need a Java build environment.  I'm developing this on Java 7 at
the moment.  You'll also need to be on Mac OS X, since the JRE doesn't include
the com.apple classes on other platforms.

You'll also need Ant.  All the other dependencies should be bundled.
If something is missing, prod me to fix it.

To build, execute 'ant' in the top directory. Files for the distribution
will appear in build/dist.


