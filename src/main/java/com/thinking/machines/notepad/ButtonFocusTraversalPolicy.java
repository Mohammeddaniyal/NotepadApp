import java.awt.*;
import java.util.*;
public class ButtonFocusTraversalPolicy extends FocusTraversalPolicy
{
private final java.util.List<Component> buttons;
public ButtonFocusTraversalPolicy(java.util.List<Component> buttons)
{
this.buttons=buttons;
}
@Override
public Component getComponentAfter(Container container,Component component)
{
int index=buttons.indexOf(component);
return buttons.get((index+1)%buttons.size());
}
@Override 
public Component getComponentBefore(Container container,Component component)
{
int index=buttons.indexOf(component);
return buttons.get((index-1+buttons.size())%(buttons.size()));
}
@Override
public Component getFirstComponent(Container container)
{
return buttons.get(0);
}
@Override
public Component getLastComponent(Container container)
{
return buttons.get(buttons.size()-1);
}
@Override 
public Component getDefaultComponent(Container container)
{
return buttons.get(0);
}
}