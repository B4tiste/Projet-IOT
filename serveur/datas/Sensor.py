import json
import time

from serveur.datas.Register import Register


class Sensor:
    """Classe représentant un capteur"""

    def __init__(self, id_sensor, name, value, type_sensor):
        self.id = id_sensor
        self.name = name
        self.value = value
        self.type = type_sensor
        self.created_at = time.strftime("%d/%m/%Y %H:%M:%S")

    @staticmethod
    def create_sensor(name, value, type_sensor):
        '''Crée un capteur'''
        return Sensor(Sensor.get_last_id(type_sensor) + 1, name, value, type_sensor)

    @staticmethod
    def load(id_sensor, type_sensor):
        '''Charge un capteur'''
        for sensor in Register.get_sensors(type_sensor):
            if sensor["id"] == id_sensor:
                return Sensor(sensor["id"], sensor["name"], sensor["value"], sensor["type"])
        return None

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

    @staticmethod
    def get_last_value(type_sensor):
        '''Retourne la dernière valeur du capteur'''
        last_sensor = Register.get_last_value(type_sensor)
        if last_sensor is not None:
            return Sensor(last_sensor["id"], last_sensor["name"], last_sensor["value"], last_sensor["type"])
        else:
            return None

    @staticmethod
    def get_sensors():
        '''Retourne les capteurs'''
        return Register.get_all_sensors_last_value()

    @staticmethod
    def get_last_id(type_sensor):
        '''Retourne le dernier id d'un capteur'''
        last_sensor = Sensor.get_last_value(type_sensor)
        if last_sensor is not None:
            return last_sensor.id
        else:
            return 0
