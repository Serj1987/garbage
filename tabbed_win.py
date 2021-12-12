from kivy.uix.tabbedpanel import TabbedPanel
from kivy.app import App
from kivy.core.window import Window
from kivymd.uix.datatables import MDDataTable

Window.size = 375, 667


class Main(TabbedPanel):
    pass


class TableWindow():
    def __init__(self, **kw):
        super().__init__(**kw)
        self.data_tables = None
        self.cur = None
        self.rows = None

    def add_table(self):
        self.con = sqlite3.connect('container.db')
        self.cur = self.con.cursor()
        self.cur.execute("SELECT * FROM details")
        self.rows = self.cur.fetchall()

        layout = AnchorLayout()
        self.data_tables = MDDataTable(
            use_pagination=True,
            column_data=[
                ("№ детали", dp(20)),
                ("Наименование", dp(30)),
                ("Количество", dp(15)),
                ("Дата", dp(20)),
                ("Примечание", dp(20)),
                ("Метка", dp(30)),
            ],
            row_data=[self.row for self.row in self.rows],
        )
        self.add_widget(self.data_tables)
        return layout

    def on_enter(self):
        self.add_table()



class TabbedApp(App):
    def build(self):
        return Main()


if __name__ == '__main__':
    TabbedApp().run()

