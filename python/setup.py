#!/user/bin/env python

from setuptools import setup, find_packages

setup(
    name='py_combinator',
    version='0.0.1',
    description='A parser combinator library written in Python',
    long_description='README.rst',
    author='Kota Mizushima',
    author_email='mizukota@gmail.com',
    install_requires=[],
    dependency_links=[],
    url='https://github.com/kmizu/py_combinator',
    license='LICENSE',
    packages=find_packages(exclude=('tests', 'docs'))
)
