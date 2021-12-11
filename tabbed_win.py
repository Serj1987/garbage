from kivy.uix.anchorlayout import AnchorLayout
from kivymd.app import MDApp
from kivy.uix.screenmanager import ScreenManager, Screen  # , WipeTransition
from kivy.core.window import Window
from kivymd.uix.datatables import MDDataTable
from kivy.metrics import dp
import sqlite3
from kivy.uix.tabbedpanel import TabbedPanel

Window.size = 375, 667


class MainMenu(Screen):
    pass


class SecondScreen(Screen):

    def btn_add_press(self):  # this func work with text from textinput and spinner
        if self.ids.number_detail.text != '':
            print(self.ids.number_detail.text, self.ids.name_detail.text,
                  self.ids.quantity.text, self.ids.comment.text)

            self.ids.number_detail.text = ''
            self.ids.name_detail.text = 'Наименование'
            self.ids.quantity.text = ''
            self.ids.comment.text = ''

class Tab(Screen, TabbedPanel):
	pass

class Browse(Screen): #, TabbedPanel): # it don't work screen with tabbedpanel

	pass

class TableWindow(Screen):
    def __init__(self, **kw):
        super().__init__(**kw)
        self.data_tables = None
        self.cur = None
        self.rows = None


class TabbedApp(MDApp):

    def build(self):
        sm = ScreenManager()  # transition=WipeTransition())
        sm.add_widget(MainMenu(name='main_menu'))
        sm.add_widget(SecondScreen(name='second_screen'))
        sm.add_widget(Browse(name='browse'))
#        sm.add_widget(Tab(name='tab'))
        return sm


if __name__ == '__main__':
    TabbedApp().run()
