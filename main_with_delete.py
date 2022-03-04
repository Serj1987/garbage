from kivy.uix.anchorlayout import AnchorLayout
from kivymd.app import MDApp
from kivy.uix.screenmanager import ScreenManager, Screen
from kivymd.uix.datatables import MDDataTable
from kivy.metrics import dp
import psycopg2
from datetime import datetime

connection_string = (
    'postgres://msojsqyh:xBl-Ab3tNu_hVXBc4ylHJbSamWhhib4j@ella.db.elephantsql.com/msojsqyh')  # ('postgres://msojsqyh:xBl-########_hVXBc4ylHJbSamWhhib4j@ella.db.elephantsql.com/msojsqyh')


class MainMenu(Screen):
    pass


class SendScreen(Screen):

    def btn_add_press(self):  # this func work with text from textinput and spinner
        if self.ids.number_detail.text != '':
            self.detail = self.ids.number_detail.text, self.ids.name_detail.text, self.ids.quantity.text, \
                          self.ids.comment.text
            con = psycopg2.connect(connection_string)  # connection string for work with psql
            cur = con.cursor()
            tag = 'отправка'
            rows = (
                self.ids.number_detail.text, self.ids.name_detail.text, self.ids.quantity.text, datetime.now(),
                self.ids.comment.text, tag)
            insert_in_sent_table = '''INSERT INTO sent(number_detail, name_detail, quantity_detail, date, note, tag) 
                                      VALUES(%s, %s, %s, %s, %s, %s) '''
            cur.execute(insert_in_sent_table, rows, )
            # cur.execute("INSERT INTO sent VALUES(%s, %s, %s, datetime.datetime.now(), %s, %s)", rows)
            # sql requests write here ^ to add one string to table

            cur.execute("SELECT * FROM sent WHERE date = DATE('now')")  # number_det/name_det/note

            all_data = cur.fetchall()
            con.commit()
            self.update_lbl_sent.text = str('Последняя деталь: ' + str(self.ids.number_detail.text) + ' ' +
                                            str(self.ids.name_detail.text) + ' ' + str(self.ids.quantity.text) +
                                            ' ' + str(self.ids.comment.text))


class ArriveScreen(Screen):
    """ this class will work with added parts in database in two tables arrived or sent parts """

    def __init__(self, **kw):
        super().__init__(**kw)
        self.detail = None

    def add_arrive(self):  # add parts into details(arrived) table
        if self.ids.number_detail.text != '':
            self.detail = self.ids.number_detail.text, self.ids.name_detail.text, self.ids.quantity.text, \
                          self.ids.comment.text
            con = psycopg2.connect(connection_string)
            cur = con.cursor()
            tag = 'приход'
            rows = (
                self.ids.number_detail.text, self.ids.name_detail.text, self.ids.quantity.text, datetime.now(),
                self.ids.comment.text, tag)
            insert_in_arrived_table = '''INSERT INTO details(number_detail, name_detail, quantity_detail, date, note, tag) 
                                                  VALUES(%s, %s, %s, %s, %s, %s) '''
            cur.execute(insert_in_arrived_table, rows, )
            # sql requests write here ^ to add one string to table

            cur.execute("SELECT * FROM details WHERE date = DATE('now')")  # number_det/name_det/note

            all_data = cur.fetchall()
            con.commit()
            self.update_lbl_arr.text = str('Последняя деталь: ' + str(self.ids.number_detail.text) + ' ' +
                                           str(self.ids.name_detail.text) + ' ' + str(self.ids.quantity.text) +
                                           ' ' + str(self.ids.comment.text))


class TableAllWindow(Screen):
    def __init__(self, **kw):
        super().__init__(**kw)
        self.con = None
        self.data_tables = None
        self.cur = None
        self.rows = None

    def add_all_table(self):
        self.con = psycopg2.connect(connection_string)
        self.cur = self.con.cursor()
        self.cur.execute("SELECT number_detail, name_detail, quantity_detail, date, note, tag FROM details UNION "
                         "SELECT number_detail, name_detail, quantity_detail, date, note, tag FROM sent ORDER BY date"
                         " DESC")
        self.rows = self.cur.fetchall()

        layout = AnchorLayout()
        self.data_tables = MDDataTable(
            use_pagination=True,
            rows_num=10,
            pagination_menu_pos='auto',
            # elevation=8,
            # pagination_menu_height = '480dp',
            size_hint=(1, 0.1),
            column_data=[
                ("№ детали", dp(20)),
                ("Наименование", dp(30)),
                ("Кол-во", dp(15)),
                ("Дата", dp(20)),
                ("Примечание", dp(20)),
                ("Метка", dp(30)),
            ],
            row_data=[self.row for self.row in self.rows],

        )
        # self.ids.all_table_layout.remove_widget(self.data_tables)
        self.ids.all_table_layout.add_widget(self.data_tables)
        return layout

    def on_enter(self):
        # self.ids.all_table_layout.remove_widget(self.data_tables)
        self.add_all_table()

    def remove_table(self):
        self.ids.all_table_layout.remove_widget(self.data_tables)


class TableDetWindow(Screen):
    def __init__(self, **kw):
        super().__init__(**kw)
        self.data_table_arrive = None
        self.data_tables = None
        self.cur = None
        self.rows = None

    def add_det_table(self):
        self.con = psycopg2.connect(connection_string)
        self.cur = self.con.cursor()
        browse = self.manager.get_screen('table_all')
        self.det = browse.ids.input_det.text
        self.cur.execute("SELECT  number_detail, name_detail, quantity_detail, date, note, tag FROM sent WHERE "
                         "number_detail = %s ORDER BY date DESC", (self.det,))
        self.rows = self.cur.fetchall()

        self.cur2 = self.con.cursor()
        self.cur2.execute("SELECT  number_detail, name_detail, quantity_detail, date, note, tag FROM details WHERE "
                          "number_detail = %s ORDER BY date DESC", (self.det,))
        self.rows2 = self.cur2.fetchall()

        layout = AnchorLayout()
        self.data_tables_sent = MDDataTable(
            use_pagination=True,
            rows_num=5,
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
        # second table
        self.data_table_arrive = MDDataTable(
            use_pagination=True,
            rows_num=5,
            column_data=[
                ("№ детали", dp(20)),
                ("Наименование", dp(30)),
                ("Количество", dp(15)),
                ("Дата", dp(20)),
                ("Примечание", dp(20)),
                ("Метка", dp(30)),
            ],
            row_data=[self.row for self.row in self.rows2],

        )
        self.ids.det_layout.add_widget(self.data_tables_sent)
        self.ids.det_layout.add_widget(self.data_table_arrive)
        #        self.add_widget(self.data_tables, index=1)
        return layout

    def on_enter(self):
        self.add_det_table()

    def remove_tables(self):
        #        self.ids.all_table_layout.remove_widget(self.data_tables)
        self.ids.det_layout.remove_widget(self.data_table_arrive)
        self.ids.det_layout.remove_widget(self.data_tables_sent)


class TableDateWindow(Screen):

    def __init__(self, **kw):
        super().__init__(**kw)
        self.cur = None
        self.con = None

    def add_date_table(self):
        self.con = psycopg2.connect(connection_string)
        self.cur = self.con.cursor()
        browse = self.manager.get_screen('table_all')
        self.date = browse.ids.input_det.text
        self.cur.execute(
            "SELECT number_detail, name_detail, quantity_detail, date, note, tag FROM sent WHERE date = %s ORDER BY date DESC",
            (self.date,))
        self.rows = self.cur.fetchall()

        self.cur2 = self.con.cursor()
        self.cur2.execute(
            "SELECT number_detail, name_detail, quantity_detail, date, note, tag FROM details WHERE date = %s ORDER BY date DESC",
            (self.date,))
        self.rows2 = self.cur2.fetchall()

        layout = AnchorLayout()
        self.data_tables = MDDataTable(
            use_pagination=True,
            rows_num=10,
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

        self.data_tables2 = MDDataTable(
            use_pagination=True,
            rows_num=10,
            column_data=[
                ("№ детали", dp(20)),
                ("Наименование", dp(30)),
                ("Количество", dp(15)),
                ("Дата", dp(20)),
                ("Примечание", dp(20)),
                ("Метка", dp(30)),
            ],
            row_data=[self.row for self.row in self.rows2],
        )

        self.ids.date_layout.add_widget(self.data_tables2)
        self.ids.date_layout.add_widget(self.data_tables)
        return layout

    def on_enter(self):
        self.add_date_table()

    def remove_tables(self):
        self.ids.date_layout.remove_widget(self.data_tables)
        self.ids.date_layout.remove_widget(self.data_tables2)


class DeleteString(Screen):
    def __init__(self, **kw):
        super().__init__(**kw)
        self.con = None
        self.data_tables = None
        self.cur = None
        self.rows = None
        self.q = None
    def delete_string_table(self):
        self.con = psycopg2.connect(connection_string)
        self.cur = self.con.cursor()
        self.cur.execute("SELECT id, number_detail, name_detail, quantity_detail, date, note, tag FROM details UNION "
                         "SELECT id, number_detail, name_detail, quantity_detail, date, note, tag FROM sent ORDER BY date"
                         " DESC")
        self.rows = self.cur.fetchall()

        layout = AnchorLayout()
        self.data_tables = MDDataTable(
            use_pagination=True,
            # rows_num=10,
            pagination_menu_pos='auto',
            check=True,
            size_hint=(1, 0.1),
            column_data=[
                ("id", dp(20)),
                ("№ детали", dp(40)),
                ("Наименование", dp(30)),
                ("Кол-во", dp(15)),
                ("Дата", dp(20)),
                ("Примечание", dp(20)),
                ("Метка", dp(30)),
            ],
            row_data=[self.row for self.row in self.rows],

        )
        # self.ids.all_table_layout.remove_widget(self.data_tables)
        # self.data_tables.bind(on_row_press=self.on_row_press)
        self.data_tables.bind(on_check_press=self.on_check_press)
        self.ids.del_string_layout.add_widget(self.data_tables)

        return layout

    def on_check_press(self, instance_table, current_row):
        '''Called when the check box in the table row is checked.'''
        print(current_row[0])
        self.id_num = current_row[0]
        self.q = self.id_num
        return self.q

    def on_enter(self):
        # self.ids.all_table_layout.remove_widget(self.data_tables)
        self.delete_string_table()

    def remove_table(self):
        self.ids.del_string_layout.remove_widget(self.data_tables)

    def on_del_press(self, on_check_press):
        q = on_check_press()
        print("id который нужен= ", q)


class CommonApp(MDApp):

    def build(self):
        sm = ScreenManager()  # transition=WipeTransition())
        sm.add_widget(MainMenu(name='main_menu'))
        sm.add_widget(SendScreen(name='send_screen'))
        sm.add_widget(ArriveScreen(name='arrive_screen'))
        sm.add_widget(TableAllWindow(name='table_all'))
        sm.add_widget(TableDetWindow(name='table_det'))
        sm.add_widget(TableDateWindow(name='table_date'))
        sm.add_widget(DeleteString(name='delete_string'))
        return sm


if __name__ == '__main__':
    CommonApp().run()
