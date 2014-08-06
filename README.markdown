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


Known bugs which I haven't found a way to fix yet
-------------------------------------------------

LayoutStyle:

* Despite the layout style correctly compensating for the visual margin of buttons,
  buttons still layout incorrectly when aligned to the edge of any other kind of component.
  (LayoutStyleAlignmentTest)

JComboBox:

* The border doesn't paint correctly over the text field of an editable combo box
  (it should surround the whole field, but only surrounds the combo box button.)
  (ComboBoxPaintingTest)

JTabbedPane:

* The currently-pressed tab still lacks the line to the left.
  (TabbedPanePaintingTest)

JMenuBar:

* The Help menu only acts like a proper help menu if the text on it is "Help" (which
  is of course only the case in English.)
  (HelpMenuTest, JI-9012514)


Hacks in use
------------

(Note: the tests in parentheses demonstrate the issue, _not_ the fix.)

JButton:

* When switching look and feel at runtime, segmented buttons no longer lose their borders.

JComboBox:

* Popup sets its JList background to match the popup itself.
* Popup renders in the correct orientation for the combo box which owns it.
* Text field on editable combo boxes lines up properly with the button.
  (ComboBoxPaintingTest)
* Text field on editable combo boxes is no longer painted in the wrong
  location when component orientation is right-to-left.

JProgressBar:

* Text is drawn at the right location vertically.
  (ProgressBarTextPositionTest)
* Baseline is correctly returned so that the progress bar will line up with other components
  in the same row.
  (ProgressBarBaselineTest)
* Indeterminate progress bars properly animate when UI is installed.
  (ProgressBarIndeterminateAnimationTest)
* Circular progress bars properly display as circular when UI is installed.
  (ProgressBarIndeterminateCircleTest)

JPopupMenu:

* Popup menus have the proper rounded corners.
  (PopupMenuRoundedCornersTest)

JList:

* JList dodges a NullPointerException which can happen in some cases when popped up from a
  combo box.

JTable:

* Rows are painted with stripes.
* Row and column spacing is set to 0 to eliminate the seams you would usually see.
* Table paints the background all the way to the bottom of the viewport it's placed inside.
* Table paints the selected rows in the proper colour when the window is inactive.

JTree:

* Selection colour paints across the entire row (known issue: animation for the disclosure
  triangle on selected rows does not occur at the moment, because of the way this hack was
  implemented.)
* Mouse clicks on the row select the node (instead of just clicks on the node.)
* Selected nodes paint using the proper inactive alternate colours when the window is inactive.
* Clicking the expander no longer momentarily paints the expander far over the left of the
  tree when component orientation is right-to-left.

JPanel:

* Panels are non-opaque by default, to fix appearance when placed inside JTabbedPane (if
  the window has apple.awt.brushMetalLook set to true, Aqua will paint the tabbed pane
  in the background colour for the window as if it's cutting through the component hierarchy.
  If it is not set, Aqua will paint the panel inside the tabbed pane as if it is opaque,
  resulting in it being slightly lighter than it should be.)
  (TabbedPanePaintingTest)

JToolBar:

* Dragging the toolbar background drags the window if apple.awt.brushMetalLook is set to
  true on its root pane.

JTabbedPane:

* The selected/pressed tab is painted one extra pixel to the left, to match the native look better.
  (TabbedPanePaintingTest)

JScrollPane:

* Scroll panes (and their contained viewports) are non-opaque by default, to fix appearance
  the pane contains a non-opaque component.

JOptionPane:

* Buttons no longer grow to become the width of the widest button.
* Better CSS stored into OptionPane.css for apps to use when displaying messages.
* JOptionPane.destructiveOption supported to mark the button which should be positioned
  away from the others.
* OK/Cancel properly flip direction for right-to-left locales.

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


