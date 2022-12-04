import json

from serveur.datas.Register import Register


class Sensor:
    """Classe représentant un capteur"""

    def __init__(self, id_sensor, name, value, type_sensor):
        self.id = id_sensor
        self.name = name
        self.value = value
        self.type = type_sensor

    def __str__(self):
        return json.dumps(self.__dict__)

    def get_name(self):
        return self.name

    def get_value(self):
        return self.value

    def set_value(self, value):
        self.value = value

    def save(self):
        '''Enregistre le capteur'''
        Register.add_sensors_data(self.type, self.__dict__)

    def get_last_value(self):
        '''Retourne la dernière valeur du capteur'''
        return Register.get_last_value(self.type)

    @staticmethod
    def get_sensors():
        '''Retourne les capteurs'''
        return Register.get_all_sensors_last_value()
