from kivy.metrics import dp
from kivy.uix.anchorlayout import AnchorLayout
import sqlite3
from kivymd.app import MDApp
from kivymd.uix.datatables import MDDataTable



class Example(MDApp):
    def build(self):
        
                
        self.c = sqlite3.connect('container.db')
        self.cur = self.c.cursor()
        self.cur.execute("SELECT * FROM details")
        self.rows = self.cur.fetchall()
       # for self.row in self.rows:
        #    self.row = self.one
        
        
        layout = AnchorLayout()
        data_tables = MDDataTable(
            size_hint=(0.9, 0.6),
            use_pagination=True,
            column_data=[
                ("№ детали", dp(30)),
                ("Наименование", dp(30)),
                ("Количество", dp(15)),
                ("Дата", dp(20)),
                ("Примечание", dp(20)),
                ("Метка", dp(30)),
            ],
            row_data=[self.row for self.row in self.rows],
        )
        layout.add_widget(data_tables)
        return layout


Example().run()

